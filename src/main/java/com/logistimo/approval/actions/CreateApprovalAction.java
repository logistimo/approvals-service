package com.logistimo.approval.actions;

import com.logistimo.approval.conversationclient.IConversationClient;
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

    Approval approval = getApproval(request);
    Approval approvalFromDB = approvalRepository.save(approval);

    createApprovalStatusHistory(approval);
    createApproversQueue(request, approval);
    createApprovalAttributes(request, approval);
    createApprovalDomainMapping(request, approval);

    return generateResponse(request, approvalFromDB);
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

  private void createApprovalDomainMapping(ApprovalRequest request, Approval approval) {
    if (!CollectionUtils.isEmpty(request.getDomains())) {
      for (Long domainId : request.getDomains()) {
        ApprovalDomainMapping domainMapping = new ApprovalDomainMapping();
        domainMapping.setApprovalId(approval.getId());
        domainMapping.setDomainId(domainId);
        approvalDomainMappingRepository.save(domainMapping);
      }
    }
  }

  private void createApprovalAttributes(ApprovalRequest request, Approval approval) {
    if (!CollectionUtils.isEmpty(request.getAttributes())) {
      for (String attributeKey : request.getAttributes().keySet()) {
        ApprovalAttributes approvalAttribute = new ApprovalAttributes();
        approvalAttribute.setApprovalId(approval.getId());
        approvalAttribute.setKey(attributeKey);
        approvalAttribute.setValue(request.getAttributes().get(attributeKey));
        approvalAttributesRepository.save(approvalAttribute);
      }
    }
  }

  private void createApproversQueue(ApprovalRequest request, Approval approval) {
    if (!CollectionUtils.isEmpty(request.getApprovers())) {
      for (Approver approver : request.getApprovers()) {
        ApproverQueue approverQueue = new ApproverQueue();
        approverQueue.setApprovalId(approval.getId());
        approverQueue.setUserId(approver.getUserId());
        approverQueue.setStartTime(approver.getStartTime());
        approverQueue.setEndTime(approver.getEndTime());
        approverQueue.setApproverStatus(Constants.QUEUED_STATUS);
        approverQueueRepository.save(approverQueue);
      }
    }
  }

  private void createApprovalStatusHistory(Approval approval) {
    ApprovalStatusHistory approvalStatusHistory = new ApprovalStatusHistory();
    approvalStatusHistory.setApprovalId(approval.getId());
    approvalStatusHistory.setStatus(approval.getStatus());
    approvalStatusHistory.setUpdatedBy(approval.getRequesterId());
//    Set the message id as the id of the message received
//    approvalStatusHistory.setMessageId();
    approvalStatusHistoryRepository.save(approvalStatusHistory);
  }

  private Approval getApproval(ApprovalRequest request) {

    String approvalId = UUID.randomUUID().toString().replace("-", "");

    String conversationId = "C123";

//    PostMessageRequest postMessageRequest = new PostMessageRequest();
//    postMessageRequest.setData("This is my message.");
//    conversationClient.postMessage(postMessageRequest, "approval", approvalId);

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
