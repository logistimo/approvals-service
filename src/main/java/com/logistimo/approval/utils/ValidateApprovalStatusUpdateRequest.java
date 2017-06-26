package com.logistimo.approval.utils;

import static com.logistimo.approval.utils.Constants.*;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApproverQueue;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.StatusUpdateRequest;
import java.util.Set;
import org.apache.catalina.connector.Response;
import org.apache.commons.lang.StringUtils;

/**
 * Created by nitisha.khandelwal on 26/06/17.
 */

public class ValidateApprovalStatusUpdateRequest {

  public static void validateRequest(String approvalId, Approval approval,
      StatusUpdateRequest request) {

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

    if (request.getUpdatedBy().equalsIgnoreCase(approval.getRequesterId()) &&
        !CANCELLED_STATUS.equalsIgnoreCase(request.getStatus())) {
      throw new BaseException(Response.SC_BAD_REQUEST, UPDATED_BY_REQUSTER_ID);
    }

    if (StringUtils.isEmpty(request.getMessageId()) && (
        REJECTED_STATUS.equalsIgnoreCase(request.getStatus()) ||
            CANCELLED_STATUS.equalsIgnoreCase(request.getStatus()))) {
      throw new BaseException(Response.SC_BAD_REQUEST, MESSAGE_ID_REQUIRED);
    }

    if (APPROVED_STATUS.equalsIgnoreCase(request.getStatus()) ||
        REJECTED_STATUS.equalsIgnoreCase(request.getStatus())) {
      ApproverQueue approver = getRequesterFromApproverQueue(request.getUpdatedBy(), approval);
      if (approver != null) {
        if (!ACTIVE_STATUS.equalsIgnoreCase(approver.getApproverStatus())) {
          throw new BaseException(Response.SC_BAD_REQUEST, REQUESTER_ID_NOT_ACTIVE);
        }
      } else {
        throw new BaseException(Response.SC_BAD_REQUEST, REQUESTER_NOT_PRESENT);
      }
    }

    if (CANCELLED_STATUS.equalsIgnoreCase(request.getStatus())) {
      if (getRequesterFromApproverQueue(request.getUpdatedBy(), approval) != null) {
        throw new BaseException(Response.SC_BAD_REQUEST, REQUESTER_ID_CANNOT_BE_AN_APPROVER);
      }
    }
  }


  private static ApproverQueue getRequesterFromApproverQueue(String requesterId, Approval approval) {
    Set<ApproverQueue> approvers = approval.getApprovers();
    for (ApproverQueue approver : approvers) {
      if (approver.getUserId().equalsIgnoreCase(requesterId)) {
        return approver;
      }
    }
    return null;
  }
}
