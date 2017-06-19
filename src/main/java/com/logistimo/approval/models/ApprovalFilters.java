package com.logistimo.approval.models;

import static com.logistimo.approval.utils.Constants.*;

import com.logistimo.approval.exception.BaseException;
import lombok.Data;
import org.apache.catalina.connector.Response;
import org.apache.commons.lang.StringUtils;

/**
 * Created by nitisha.khandelwal on 12/06/17.
 */

@Data
public class ApprovalFilters {

  private int offset;

  private int size;

  private String type;

  private String typeId;

  private String status;

  private String expiringInMinutes;

  private String requesterId;

  private String approverId;

  private String approverStatus;

  private String attributeKey;

  private String attributeValue;

  private int domainId;

  private String orderedBy;

  public void validate() {

    if (StringUtils.isNotEmpty(status) && StringUtils.isNotEmpty(expiringInMinutes) &&
        !StringUtils.equalsIgnoreCase(PENDING_STATUS, status)) {
      throw new BaseException(Response.SC_BAD_REQUEST, INVALID_STATUS_AND_EXPIRING_IN_COMBINATION);
    }

    if (StringUtils.isNotEmpty(approverStatus) && StringUtils.isEmpty(approverId)) {
      throw new BaseException(Response.SC_BAD_REQUEST, APPROVER_ID_NOT_PRESENT);
    }

    if ((StringUtils.isNotEmpty(attributeKey) && StringUtils.isEmpty(attributeValue))
        || StringUtils.isNotEmpty(attributeValue) && StringUtils.isEmpty(attributeKey)) {
      throw new BaseException(Response.SC_BAD_REQUEST, KEY_OR_VALUE_MISSING);
    }

    if (StringUtils.isNotEmpty(expiringInMinutes)) {
      setStatus(PENDING_STATUS);
    }
  }
}
