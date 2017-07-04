package com.logistimo.approval.utils;

/**
 * Created by nitisha.khandelwal on 12/05/17.
 */

public class Constants {

  public static final String COMMA = ",";
  public static final String COLON = ":";
  public static final String ASCENDING = "ASC";
  public static final String DESCENDING = "DESC";
  public static final String DEFAULT_SORT_ORDER = "createdAt:DESC";

  public static final String CONVERSATION_TYPE = "APPROVAL";

  public static final String STATUS_UPDATED_BY = "CRON_JOB";

  public static final String APPROVAL_ALREADY_EXITS = "Approval already exits for the %s - %s in %s state.";
  public static final String STATUS_HISTORY_NOT_FOUND = "Status History not found for the approval with the id - ";

  public static final String MESSAGE_ID_REQUIRED = "Message Id is required for the rejection and cancellation of the approval.";
  public static final String REQUESTER_ID_NOT_ACTIVE = "Requester Id is not in the ACTIVE approver queue.";
  public static final String REQUESTER_NOT_PRESENT = "Requester Id is not present in any of the approver queue.";
  public static final String UPDATED_BY_REQUSTER_ID = "Status of the approval cannot be updated by the requester of the approval.";
  public static final String REQUESTER_ID_CANNOT_BE_AN_APPROVER = "Requester Id cannot be an approver in case of cancellation.";

  public static final String APPROVAL_NOT_FOUND = "Approval with the id - %s not found.";
  public static final String APPROVAL_NOT_PENDING = "Approval is not in PENDING state, so the state transition is invalid.";
  public static final String INVALID_STATUS_AND_EXPIRING_IN_COMBINATION = "The param - expiring_in can only be used with PENDING status.";
  public static final String KEY_OR_VALUE_MISSING = "Either Attribute key or value is missing.";
  public static final String APPROVER_ID_NOT_PRESENT = "The param - approver_status cannot be present without approver_id.";
  public static final String INCORRECT_SORT_PARAM = "Either ASC or DESC missing from the one of the Sort params.";

  public static final String EXPIRY_TASK = "EXPIRY";
  public static final String ACTIVATION_TASK = "ACTIVATION";
  public static final String TASK_QUEUED = "qd";
  public static final String TASK_ACTIVE = "ac";
  public static final String TASK_DONE = "dn";

  public static final String QUEUED_STATUS = "qd";
  public static final String ACTIVE_STATUS = "ac";
  public static final String EXPIRED_STATUS = "ex";
  public static final String PENDING_STATUS = "pn";
  public static final String APPROVED_STATUS = "ap";
  public static final String REJECTED_STATUS = "rj";
  public static final String CANCELLED_STATUS = "cn";

  public static final String PENDING_OR_APPROVED_STATUS = "PENDING OR APPROVED";

}
