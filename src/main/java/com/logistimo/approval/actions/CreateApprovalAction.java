package com.logistimo.approval.actions;

import static com.logistimo.approval.utils.Constants.*;

import com.logistimo.approval.conversationclient.request.PostMessageResponse;
import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalAttributes;
import com.logistimo.approval.entity.ApprovalDomainMapping;
import com.logistimo.approval.entity.ApprovalStatusHistory;
import com.logistimo.approval.entity.ApproverQueue;
import com.logistimo.approval.entity.Task;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.ApprovalRequest;
import com.logistimo.approval.models.ApprovalResponse;
import com.logistimo.approval.models.ApproverRequest;
import com.logistimo.approval.models.ApproverResponse;
import com.logistimo.approval.repository.IApprovalAttributesRepository;
import com.logistimo.approval.repository.IApprovalDomainMappingRepository;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApprovalStatusHistoryRepository;
import com.logistimo.approval.repository.IApproverQueueRepository;
import com.logistimo.approval.repository.ITaskRepository;
import com.logistimo.approval.utils.Constants;
import com.logistimo.approval.utils.Utility;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.catalina.connector.Response;
import org.apache.commons.lang.time.DateUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

@Component
public class CreateApprovalAction {

  @Autowired
  private IApprovalRepository approvalRepository;

  @Autowired
  private IApprovalStatusHistoryRepository approvalStatusHistoryRepository;

  @Autowired
  private IApproverQueueRepository approverQueueRepository;

  @Autowired
  private IApprovalAttributesRepository approvalAttributesRepository;

  @Autowired
  private IApprovalDomainMappingRepository approvalDomainMappingRepository;

  @Autowired
  private ITaskRepository taskRepository;

  @Autowired
  private Utility utility;

  private static final Logger log = LoggerFactory.getLogger(CreateApprovalAction.class);

  public ApprovalResponse invoke(ApprovalRequest request) {

    log.info("Inside the CreateApprovalAction with request - {}", request);

    List<Approval> approvals = approvalRepository
        .findApprovedOrPendingApprovalsByTypeAndTypeId(request.getType(), request.getTypeId());

    if (!CollectionUtils.isEmpty(approvals)) {
      throw new BaseException(Response.SC_CONFLICT, String.format(APPROVAL_ALREADY_EXITS,
          request.getType(), request.getTypeId(), PENDING_OR_APPROVED_STATUS));
    }

    String approvalId = UUID.randomUUID().toString().replace("-", "");

    PostMessageResponse postMessageResponse = utility.addMessageToConversation(approvalId,
        request.getMessage(), request.getRequesterId(), request.getSourceDomainId());

    Approval approval = getApproval(approvalId, request, postMessageResponse.getConversationId());
    Approval approvalFromDB = approvalRepository.save(approval);

    createApprovalStatusHistory(approval, postMessageResponse.getMessageId());
    createApproversQueue(approvalFromDB, request);
    createApprovalAttributes(approvalId, request);
    createApprovalDomainMapping(approvalId, request);

    return generateResponse(request, approvalFromDB);
  }

  private void createApprovalDomainMapping(String approvalId, ApprovalRequest request) {

    Optional.ofNullable(request.getDomains()).ifPresent(l -> l.forEach(
        item -> approvalDomainMappingRepository.save(new ApprovalDomainMapping(approvalId, item))));
  }

  private ApprovalResponse generateResponse(ApprovalRequest request, Approval approval) {

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setAmbiguityIgnored(true);

    ApprovalResponse response = mapper.map(request, ApprovalResponse.class);
    response.setConversationId(approval.getConversationId());
    response.setApprovalId(approval.getId());
    response.setStatus(approval.getStatus());
    response.setExpireAt(approval.getExpireAt());
    response.setCreatedAt(approval.getCreatedAt());
    response.setUpdatedAt(approval.getUpdatedAt());
    response.setActiveApproverType(request.getApprovers().get(0).getType());
    response.setApprovers(new ArrayList<>());
    Optional.ofNullable(approverQueueRepository.findByApprovalId(approval.getId())).ifPresent(
        l -> l.forEach(x -> response.getApprovers().add(mapper.map(x, ApproverResponse.class))));

    return response;
  }

  private void createApprovalAttributes(String approvalId, ApprovalRequest request) {
    if (!CollectionUtils.isEmpty(request.getAttributes())) {
      for (String attributeKey : request.getAttributes().keySet()) {
        approvalAttributesRepository.save(new ApprovalAttributes(approvalId, attributeKey,
            request.getAttributes().get(attributeKey)));
      }
    }
  }

  private void createApproversQueue(Approval approval, ApprovalRequest request) {

    if (!CollectionUtils.isEmpty(request.getApprovers())) {

      Long queueId = 1L;
      Date startTime = approval.getCreatedAt();
      for (ApproverRequest approver : request.getApprovers()) {

        Date expiryTime = DateUtils.addHours(startTime, approver.getExpiry());
        String status = Constants.QUEUED_STATUS;

        if (queueId == 1L) {
          status = Constants.ACTIVE_STATUS;
        }

        for (String userId : approver.getUserIds()) {
          approverQueueRepository.save(new ApproverQueue(approval.getId(), userId, status,
              approver.getType(), queueId, startTime, expiryTime));
        }

        taskRepository.save(new Task(approval.getId(), queueId, EXPIRY_TASK, expiryTime, TASK_QUEUED));

        startTime = DateUtils.addHours(startTime, approver.getExpiry());
        queueId += 1;
      }
    }
  }

  private void createApprovalStatusHistory(Approval approval, String messageId) {

    approvalStatusHistoryRepository.save(new ApprovalStatusHistory(approval.getId(),
        approval.getStatus(), approval.getRequesterId(), messageId, null));
  }

  private Approval getApproval(String approvalId, ApprovalRequest request, String conversationId) {

    Approval approval = new Approval();
    approval.setId(approvalId);
    approval.setStatus(Constants.PENDING_STATUS);
    approval.setConversationId(conversationId);
    approval.setType(request.getType());
    approval.setTypeId(request.getTypeId());
    approval.setRequesterId(request.getRequesterId());
    approval.setSourceDomainId(request.getSourceDomainId());
    approval.setApproverQueuesCount(Long.valueOf(request.getApprovers().size()));
    approval.setUpdatedBy(request.getRequesterId());
    approval.setExpireAt(DateUtils.addHours(new Date(), getTotalExpiryHours(request)));
    return approval;
  }

  private int getTotalExpiryHours(ApprovalRequest request) {

    int totalExpiryHours = 0;
    for (ApproverRequest approverRequest : request.getApprovers()) {
      totalExpiryHours += approverRequest.getExpiry();
    }
    return totalExpiryHours;
  }
}
