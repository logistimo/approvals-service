package com.logistimo.approval.actions;


import static com.logistimo.approval.utils.Constants.*;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalStatusHistory;
import com.logistimo.approval.entity.ApproverQueue;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.ApprovalStatusUpdateEvent;
import com.logistimo.approval.models.StatusUpdateRequest;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApprovalStatusHistoryRepository;
import com.logistimo.approval.utils.JmsUtil;
import java.util.Date;
import java.util.Set;
import org.apache.catalina.connector.Response;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Created by nitisha.khandelwal on 12/05/17.
 */

@Component
public class UpdateApprovalStatusAction {

  @Autowired
  private IApprovalRepository approvalRepository;

  @Autowired
  private IApprovalStatusHistoryRepository statusHistoryRepository;

  @Autowired
  private JmsUtil jmsUtil;

  public Void invoke(String approvalId, StatusUpdateRequest request) {

    Approval approval = approvalRepository.findOne(approvalId);

    validateRequest(approvalId, approval, request);

    approval.setStatus(request.getStatus());
    approval.setUpdatedBy(request.getUpdatedBy());
    approvalRepository.save(approval);

    Date now = new Date();

    ApprovalStatusHistory lastStatus = statusHistoryRepository
        .findLastUpdateByApprovalId(approvalId);
    lastStatus.setEndTime(now);
    statusHistoryRepository.save(lastStatus);

    ApprovalStatusHistory currentStatus = statusHistoryRepository.save(
        new ApprovalStatusHistory(approvalId, request.getStatus(),
            request.getUpdatedBy(), request.getMessageId(), now));

    jmsUtil.sendMessage(new ApprovalStatusUpdateEvent(approval.getId(),
        approval.getType(), approval.getTypeId(), approval.getStatus(),
        approval.getUpdatedBy(), currentStatus.getStartTime()));

    return null;
  }

  private void validateRequest(String approvalId, Approval approval, StatusUpdateRequest request) {

    if (approval == null) {
      throw new BaseException(Response.SC_NOT_FOUND, String.format(APPROVAL_NOT_FOUND, approvalId));
    }

    if ((APPROVED_STATUS.equalsIgnoreCase(request.getStatus()) ||
        REJECTED_STATUS.equalsIgnoreCase(request.getStatus()) ||
        CANCELLED_STATUS.equalsIgnoreCase(request.getStatus()) ||
        EXPIRED_STATUS.equalsIgnoreCase(request.getStatus())) &&
        !(PENDING_STATUS.equalsIgnoreCase(approval.getStatus()))) {
      throw new BaseException(Response.SC_BAD_REQUEST, APPROVAL_NOT_PENDING);
    }

    if (StringUtils.isEmpty(request.getMessageId()) &&
        (REJECTED_STATUS.equalsIgnoreCase(request.getStatus()) ||
            CANCELLED_STATUS.equalsIgnoreCase(request.getStatus()))) {
      throw new BaseException(Response.SC_BAD_REQUEST, MESSAGE_ID_REQUIRED);
    }

    if (request.getUpdatedBy().equalsIgnoreCase(approval.getRequesterId())) {
      throw new BaseException(Response.SC_BAD_REQUEST, UPDATED_BY_REQUSTER_ID);
    }

    Set<ApproverQueue> approvers = approval.getApprovers();
    Boolean requsterIdPresentInApproverQueue = Boolean.FALSE;

    if (!CollectionUtils.isEmpty(approvers)) {
      for (ApproverQueue approver : approvers) {
        if (approver.getUserId().equalsIgnoreCase(request.getUpdatedBy())) {
          requsterIdPresentInApproverQueue = Boolean.TRUE;
          if (!ACTIVE_STATUS.equalsIgnoreCase(approver.getApproverStatus())) {
            throw new BaseException(Response.SC_BAD_REQUEST, REQUESTER_ID_NOT_ACTIVE);
          }
        }
      }
    } else {
      throw new BaseException(Response.SC_BAD_REQUEST, APPROVER_NOT_CONFIGURED);
    }

    if (!requsterIdPresentInApproverQueue) {
      throw new BaseException(Response.SC_BAD_REQUEST, REQUESTER_NOT_PRESENT);
    }
  }
}
