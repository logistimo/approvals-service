package com.logistimo.approval.utils;

/**
 * Created by nitisha.khandelwal on 12/05/17.
 */

public class Constants {

  public static final String APPROVAL_NOT_FOUND = "Approval with the id - %s not found.";
  public static final String INVALID_STATUS_AND_EXPIRING_IN_COMBINATION = "The param - expiring_in can only be used with PENDING status.";
  public static final String KEY_OR_VALUE_MISSING = "Either Attribute key or value is missing.";
  public static final String APPROVER_ID_NOT_PRESENT = "The param - approver_status cannot be present without approver_id.";

  public static final String QUEUED_STATUS = "QUEUED";
  public static final String ACTIVE_STATUS = "ACTIVE";
  public static final String PENDING_STATUS = "PENDING";
  public static final String APPROVED_STATUS = "APPROVED";
  public static final String PENDING_OR_APPROVED_STATUS = "PENDING OR APPROVED";

}
