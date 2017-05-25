package com.logistimo.approval.actions;

import com.logistimo.approval.conversationclient.IConversationClient;
import com.logistimo.approval.conversationclient.request.PostMessageResponse;
import com.logistimo.approval.conversationclient.response.PostMessageRequest;
import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalAttributes;
import com.logistimo.approval.entity.ApprovalDomainMapping;
import com.logistimo.approval.entity.ApprovalStatusHistory;
import com.logistimo.approval.entity.ApproverQueue;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.ApprovalRequest;
import com.logistimo.approval.models.Approver;
import com.logistimo.approval.models.ApprovalResponse;
import com.logistimo.approval.repository.IApprovalAttributesRepository;
import com.logistimo.approval.repository.IApprovalDomainMappingRepository;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApprovalStatusHistoryRepository;
import com.logistimo.approval.repository.IApproverQueueRepository;
import com.logistimo.approval.utils.Constants;
import java.util.List;
import java.util.UUID;
import org.apache.catalina.connector.Response;
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

  private static final String APPROVAL_ALREADY_EXITS = "Approval already exits for the %s - %s in %s state.";

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
  private IConversationClient conversationClient;

  private static final Logger log = LoggerFactory.getLogger(CreateApprovalAction.class);

  public ApprovalResponse invoke(ApprovalRequest request) {

    log.info("Inside the CreateApprovalAction with request - {}", request);

    List<Approval> approvals = approvalRepository.findByTypeAndTypeId(request.getType(),
        request.getTypeId());

    if (!CollectionUtils.isEmpty(approvals)) {
      for (Approval approval : approvals) {
        if (Constants.PENDING_STATUS.equalsIgnoreCase(approval.getStatus()) ||
            Constants.APPROVED_STATUS.equalsIgnoreCase(approval.getStatus())) {
          throw new BaseException(Response.SC_BAD_REQUEST, String.format(APPROVAL_ALREADY_EXITS,
              approval.getType(), approval.getTypeId(), approval.getStatus()));
        }
      }
    }

    String approvalId = UUID.randomUUID().toString().replace("-", "");

    PostMessageResponse postMessageResponse = createConversation(approvalId, request);

    Approval approval = getApproval(approvalId, request, postMessageResponse.getConversationId());
    Approval approvalFromDB = approvalRepository.save(approval);

    createApprovalStatusHistory(approval, postMessageResponse.getMessageId());
    createApproversQueue(approvalId,request);
    createApprovalAttributes(approvalId, request);
    createApprovalDomainMapping(approvalId, request);

    return generateResponse(request, approvalFromDB);
  }

  private PostMessageResponse createConversation(String approvalId, ApprovalRequest request) {
    PostMessageRequest postMessageRequest = new PostMessageRequest(request.getMessage(),
        request.getRequesterId(), request.getSourceDomainId());
    return conversationClient.postMessage(postMessageRequest, "APPROVAL", approvalId);
  }

  private ApprovalResponse generateResponse(ApprovalRequest request, Approval approval) {

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setAmbiguityIgnored(true);

    ApprovalResponse response = mapper.map(request, ApprovalResponse.class);
    response.setApprovalId(approval.getId());
    response.setStatus(approval.getStatus());
    response.setCreatedAt(approval.getCreatedAt());
    response.setUpdatedAt(approval.getUpdatedAt());

    return response;
  }

  private void createApprovalDomainMapping(String approvalId, ApprovalRequest request) {
    if (!CollectionUtils.isEmpty(request.getDomains())) {
      for (Long domainId : request.getDomains()) {
        ApprovalDomainMapping domainMapping = new ApprovalDomainMapping();
        domainMapping.setApprovalId(approvalId);
        domainMapping.setDomainId(domainId);
        approvalDomainMappingRepository.save(domainMapping);
      }
    }
  }

  private void createApprovalAttributes(String approvalId, ApprovalRequest request) {
    if (!CollectionUtils.isEmpty(request.getAttributes())) {
      for (String attributeKey : request.getAttributes().keySet()) {
        ApprovalAttributes approvalAttribute = new ApprovalAttributes();
        approvalAttribute.setApprovalId(approvalId);
        approvalAttribute.setKey(attributeKey);
        approvalAttribute.setValue(request.getAttributes().get(attributeKey));
        approvalAttributesRepository.save(approvalAttribute);
      }
    }
  }

  private void createApproversQueue(String approvalId, ApprovalRequest request) {
    if (!CollectionUtils.isEmpty(request.getApprovers())) {
      for (Approver approver : request.getApprovers()) {
        ApproverQueue approverQueue = new ApproverQueue();
        approverQueue.setApprovalId(approvalId);
        approverQueue.setUserId(approver.getUserId());
        approverQueue.setStartTime(approver.getStartTime());
        approverQueue.setEndTime(approver.getEndTime());
        approverQueue.setApproverStatus(Constants.QUEUED_STATUS);
        approverQueueRepository.save(approverQueue);
      }
    }
  }

  private void createApprovalStatusHistory(Approval approval, String messageId) {
    ApprovalStatusHistory approvalStatusHistory = new ApprovalStatusHistory();
    approvalStatusHistory.setApprovalId(approval.getId());
    approvalStatusHistory.setStatus(approval.getStatus());
    approvalStatusHistory.setUpdatedBy(approval.getRequesterId());
    approvalStatusHistory.setMessageId(messageId);
    approvalStatusHistoryRepository.save(approvalStatusHistory);
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
    approval.setUpdatedBy(request.getRequesterId());
    approval.setExpireAt(request.getExpireAt());

    return approval;
  }

}
