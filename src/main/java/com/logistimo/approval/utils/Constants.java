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

  public static final String APPROVAL_ALREADY_EXITS = "AS005";
  public static final String STATUS_HISTORY_NOT_FOUND = "AS012";

  public static final String MESSAGE_ID_REQUIRED = "AS008";
  public static final String REQUESTER_ID_NOT_ACTIVE = "AS009";
  public static final String REQUESTER_NOT_PRESENT = "AS010";
  public static final String UPDATED_BY_REQUSTER_ID = "AS013";
  public static final String REQUESTER_ID_CANNOT_BE_AN_APPROVER = "AS011";

  public static final String APPROVAL_NOT_FOUND = "AS006";
  public static final String APPROVAL_NOT_PENDING = "AS007";
  public static final String INVALID_STATUS_AND_EXPIRING_IN_COMBINATION = "AS001";
  public static final String KEY_OR_VALUE_MISSING = "AS003";
  public static final String APPROVER_ID_NOT_PRESENT = "AS002";
  public static final String INCORRECT_SORT_PARAM = "AS004";

  public static final String EXPIRY_TASK = "EXPIRY";
  public static final String ACTIVATION_TASK = "ACTIVATION";
  public static final String TASK_QUEUED = "qd";
  public static final String TASK_ACTIVE = "ac";
  public static final String TASK_DONE = "dn";
  public static final String TASK_NOT_REQUIRED = "nr";


  public static final String QUEUED_STATUS = "qd";
  public static final String ACTIVE_STATUS = "ac";
  public static final String EXPIRED_STATUS = "ex";
  public static final String PENDING_STATUS = "pn";
  public static final String APPROVED_STATUS = "ap";
  public static final String REJECTED_STATUS = "rj";
  public static final String CANCELLED_STATUS = "cn";

  public static final String PENDING_OR_APPROVED_STATUS = "PENDING OR APPROVED";

}
