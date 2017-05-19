package com.logistimo.approval.actions;

import static org.springframework.data.jpa.domain.Specifications.where;

import com.logistimo.approval.config.ApprovalSpecifications;
import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.repository.IApprovalCustomRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * Created by nitisha.khandelwal on 18/05/17.
 */

@Component
public class GetFilteredApprovalsAction {

  @Autowired
  private IApprovalCustomRepository approvalCustomRepository;

  public Page<Approval> invoke(int offset, int size, String requesterId, String status,
      Long expiringInMinutes, String approverId, String approverStatus, String type,
      String typeId, String orderedBy, String attributeKey, String attributeValue) {

    Pageable pageable = new PageRequest(offset, size);

    Page<Approval> approvals = approvalCustomRepository
        .findAll(where(ApprovalSpecifications.withRequesterId(requesterId))
                .and(ApprovalSpecifications.withStatus(status))
                .and(ApprovalSpecifications.withType(type))
                .and(ApprovalSpecifications.withTypeId(typeId))
                .and(ApprovalSpecifications.withApproverId(approverId))
                .and(ApprovalSpecifications.withApproverStatus(approverId, approverStatus))
                .and(ApprovalSpecifications.withApprovalKey(attributeKey, attributeValue))
                .and(ApprovalSpecifications.withApprovalValue(attributeKey, attributeValue)),
            pageable);

    return approvals;
  }
}
