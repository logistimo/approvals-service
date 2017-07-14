package com.logistimo.approval.actions;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApproverQueue;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.ApprovalResponse;
import com.logistimo.approval.models.ApproverResponse;
import com.logistimo.approval.repository.IApprovalRepository;

import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Optional;
import java.util.Set;

import static com.logistimo.approval.utils.Constants.ACTIVE_STATUS;
import static com.logistimo.approval.utils.Constants.APPROVAL_NOT_FOUND;
import static com.logistimo.approval.utils.Constants.PENDING_STATUS;

/**
 * Created by nitisha.khandelwal on 11/05/17.
 */

@Component
public class GetApprovalAction {

  @Autowired
  private IApprovalRepository approvalRepository;

  public ApprovalResponse invoke(String approvalId) {

    Approval approval = approvalRepository.findOne(approvalId);

    if (approval == null) {
      throw new BaseException(Response.SC_NOT_FOUND, APPROVAL_NOT_FOUND, approvalId);
    }

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setAmbiguityIgnored(true);

    ApprovalResponse response = getResponseFromApprovalDB(approval);

    if (PENDING_STATUS.equalsIgnoreCase(approval.getStatus())) {
      Set<ApproverQueue> queues = approval.getApprovers();
      if (!CollectionUtils.isEmpty(queues)) {
        for (ApproverQueue queue : queues) {
          if (ACTIVE_STATUS.equalsIgnoreCase(queue.getApproverStatus())) {
            response.setActiveApproverType(queue.getType());
            response.getApprovers().add(mapper.map(queue, ApproverResponse.class));
          }
        }
      }
    }

    Optional.ofNullable(approval.getDomains()).ifPresent(
        l -> l.forEach(item -> response.getDomains().add(item.getDomainId())));

    Optional.ofNullable(approval.getAttributes()).ifPresent(
        l -> l.forEach(item -> response.getAttributes().put(item.getKey(), item.getValue())));

    return response;
  }

  private ApprovalResponse getResponseFromApprovalDB(Approval approval) {
    ApprovalResponse response = new ApprovalResponse();
    response.setApprovalId(approval.getId());
    response.setType(approval.getType());
    response.setTypeId(approval.getTypeId());
    response.setRequesterId(approval.getRequesterId());
    response.setStatus(approval.getStatus());
    response.setConversationId(approval.getConversationId());
    response.setSourceDomainId(approval.getSourceDomainId());
    response.setExpireAt(approval.getExpireAt());
    response.setCreatedAt(approval.getCreatedAt());
    response.setUpdatedAt(approval.getUpdatedAt());
    response.setUpdatedBy(approval.getUpdatedBy());
    return response;
  }
}
