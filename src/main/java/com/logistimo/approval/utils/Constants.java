package com.logistimo.approval.utils;

/**
 * Created by nitisha.khandelwal on 12/05/17.
 */

public class Constants {

  public static final String APPROVAL_ALREADY_EXITS = "Approval already exits for the %s - %s in %s state.";
  public static final String STATUS_HISTORY_NOT_FOUND = "Status History not found for the approval with the id - ";


  public static final String MESSAGE_ID_REQUIRED = "Message Id is required for the rejection and cancellation of the approval.";
  public static final String REQUESTER_ID_NOT_ACTIVE = "Requester Id is not in the ACTIVE approver queue.";
  public static final String APPROVER_NOT_CONFIGURED = "Approver Queue have not been configured for this approval.";
  public static final String REQUESTER_NOT_PRESENT = "Requester Id is not present in any of the approver queue.";
  public static final String UPDATED_BY_REQUSTER_ID = "Status of the approval cannot be updated by the requester of the approval.";

  public static final String APPROVAL_NOT_FOUND = "Approval with the id - %s not found.";
  public static final String APPROVAL_NOT_PENDING = "Approval is not in PENDING state, so the state transition is invalid.";
  public static final String INVALID_STATUS_AND_EXPIRING_IN_COMBINATION = "The param - expiring_in can only be used with PENDING status.";
  public static final String KEY_OR_VALUE_MISSING = "Either Attribute key or value is missing.";
  public static final String APPROVER_ID_NOT_PRESENT = "The param - approver_status cannot be present without approver_id.";

  public static final String QUEUED_STATUS = "QD";
  public static final String ACTIVE_STATUS = "AC";
  public static final String EXPIRED_STATUS = "EX";
  public static final String PENDING_STATUS = "PN";
  public static final String APPROVED_STATUS = "AP";
  public static final String REJECTED_STATUS = "RJ";
  public static final String CANCELLED_STATUS = "CN";

  public static final String PENDING_OR_APPROVED_STATUS = "PENDING OR APPROVED";

}
