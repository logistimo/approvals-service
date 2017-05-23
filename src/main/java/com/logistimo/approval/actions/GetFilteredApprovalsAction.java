package com.logistimo.approval.actions;

import static com.logistimo.approval.utils.Constants.*;
import static org.springframework.data.jpa.domain.Specifications.where;

import com.logistimo.approval.config.ApprovalSpecifications;
import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.repository.IApprovalCustomRepository;
import org.apache.catalina.connector.Response;
import org.apache.commons.lang.StringUtils;
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
      String expiringInMinutes, String approverId, String approverStatus, String type,
      String typeId, String orderedBy, String attributeKey, String attributeValue) {

    if (StringUtils.isNotEmpty(expiringInMinutes) && StringUtils.isNotEmpty(status)
        && !StringUtils.equalsIgnoreCase(PENDING_STATUS, status)) {
      throw new BaseException(Response.SC_BAD_REQUEST, INVALID_STATUS_AND_EXPIRING_IN_COMBINATION);
    }

    if (StringUtils.isNotEmpty(approverStatus) && StringUtils.isEmpty(approverId)) {
      throw new BaseException(Response.SC_BAD_REQUEST, APPROVER_ID_NOT_PRESENT);
    }

    if ((StringUtils.isNotEmpty(attributeKey) && StringUtils.isEmpty(attributeValue)) ||
        StringUtils.isNotEmpty(attributeValue) && StringUtils.isEmpty(attributeKey)) {
      throw new BaseException(Response.SC_BAD_REQUEST, KEY_OR_VALUE_MISSING);
    }

    if (StringUtils.isNotEmpty(expiringInMinutes)) {
      status = PENDING_STATUS;
    }

    Pageable pageable = new PageRequest(offset, size);

    Page<Approval> approvals = approvalCustomRepository.findAll(
        where(ApprovalSpecifications.withRequesterId(requesterId))
            .and(ApprovalSpecifications.withStatus(status))
            .and(ApprovalSpecifications.withType(type))
            .and(ApprovalSpecifications.withTypeId(typeId))
            .and(ApprovalSpecifications.withApproverId(approverId))
            .and(ApprovalSpecifications.withExpiringInMinutes(expiringInMinutes))
            .and(ApprovalSpecifications.withApproverStatus(approverStatus))
            .and(ApprovalSpecifications.withApprovalKey(attributeKey))
            .and(ApprovalSpecifications.withApprovalValue(attributeValue)), pageable);

    return approvals;
  }
}
