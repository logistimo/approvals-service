package com.logistimo.approval.actions;

import static org.springframework.data.jpa.domain.Specifications.where;

import com.logistimo.approval.config.ApprovalSpecifications;
import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.models.ApprovalFilters;
import com.logistimo.approval.repository.IApprovalCustomRepository;
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

  public Page<Approval> invoke(ApprovalFilters filters) {

    Pageable pageable = new PageRequest(filters.getOffset(), filters.getSize());

    return approvalCustomRepository.findAll(
        where(ApprovalSpecifications.withRequesterId(filters.getRequesterId()))
            .and(ApprovalSpecifications.withDomainId(Long.valueOf(filters.getDomainId())))
            .and(ApprovalSpecifications.withStatus(filters.getStatus()))
            .and(ApprovalSpecifications.withType(filters.getType()))
            .and(ApprovalSpecifications.withTypeId(filters.getTypeId()))
            .and(ApprovalSpecifications.withApproverId(filters.getApproverId()))
            .and(ApprovalSpecifications.withExpiringInMinutes(filters.getExpiringInMinutes()))
            .and(ApprovalSpecifications.withApproverStatus(filters.getApproverStatus()))
            .and(ApprovalSpecifications.withApprovalKey(filters.getAttributeKey()))
            .and(ApprovalSpecifications.withApprovalValue(filters.getAttributeValue())), pageable);
  }
}
