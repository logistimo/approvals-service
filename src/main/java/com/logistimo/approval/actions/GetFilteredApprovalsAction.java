package com.logistimo.approval.actions;

import com.logistimo.approval.config.ApprovalSpecifications;
import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.models.ApprovalFilters;
import com.logistimo.approval.repository.IApprovalCustomRepository;

import com.logistimo.approval.utils.OffsetBasedPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Created by nitisha.khandelwal on 18/05/17.
 */

@Component
public class GetFilteredApprovalsAction {

  @Autowired
  private IApprovalCustomRepository approvalCustomRepository;

  public Page<Approval> invoke(ApprovalFilters filters) {

    Pageable pageable = new OffsetBasedPageRequest(filters.getOffset(), filters.getSize(),
        filters.getSort());

    return approvalCustomRepository.findAll(
        where(ApprovalSpecifications.withRequesterId(filters.getRequesterId()))
            .and(ApprovalSpecifications.withDomainId(filters.getDomainId()))
            .and(ApprovalSpecifications.withStatus(filters.getStatus()))
            .and(ApprovalSpecifications.withType(filters.getType()))
            .and(ApprovalSpecifications.withTypeId(filters.getTypeId()))
            .and(ApprovalSpecifications.withApproverId(filters.getApproverId()))
            .and(ApprovalSpecifications.withExpiringInMinutes(filters.getExpiringInMinutes()))
            .and(ApprovalSpecifications.withApproverStatus(filters.getApproverStatus()))
            .and(ApprovalSpecifications.withAttributes(filters.getAttributes()))
        , pageable);
  }
}
