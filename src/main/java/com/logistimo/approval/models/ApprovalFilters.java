package com.logistimo.approval.models;

import com.logistimo.approval.exception.BaseException;

import org.apache.catalina.connector.Response;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

import static com.logistimo.approval.utils.Constants.APPROVER_ID_NOT_PRESENT;
import static com.logistimo.approval.utils.Constants.ASCENDING;
import static com.logistimo.approval.utils.Constants.COLON;
import static com.logistimo.approval.utils.Constants.COMMA;
import static com.logistimo.approval.utils.Constants.DEFAULT_SORT_ORDER;
import static com.logistimo.approval.utils.Constants.DESCENDING;
import static com.logistimo.approval.utils.Constants.INCORRECT_SORT_PARAM;
import static com.logistimo.approval.utils.Constants.INVALID_STATUS_AND_EXPIRING_IN_COMBINATION;
import static com.logistimo.approval.utils.Constants.KEY_OR_VALUE_MISSING;
import static com.logistimo.approval.utils.Constants.PENDING_STATUS;

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

  private Integer expiringInMinutes;

  private String requesterId;

  private String approverId;

  private String approverStatus;

  private List<AttributeFilter> attributes = new ArrayList<>(1);

  private Long domainId;

  private String sortQuery;

  private Sort sort;

  public void validate() {

    if (StringUtils.isNotEmpty(status) && expiringInMinutes != null &&
        !StringUtils.equalsIgnoreCase(PENDING_STATUS, status)) {
      throw new BaseException(Response.SC_BAD_REQUEST, INVALID_STATUS_AND_EXPIRING_IN_COMBINATION);
    }

    if (StringUtils.isNotEmpty(approverStatus) && StringUtils.isEmpty(approverId)) {
      throw new BaseException(Response.SC_BAD_REQUEST, APPROVER_ID_NOT_PRESENT);
    }

    for (AttributeFilter attribute : attributes) {
      if ((StringUtils.isNotEmpty(attribute.getKey()) && attribute.getValues().isEmpty())
          || !attribute.getValues().isEmpty() && StringUtils.isEmpty(attribute.getKey())) {
        throw new BaseException(Response.SC_BAD_REQUEST, KEY_OR_VALUE_MISSING);
      }
    }

    if (expiringInMinutes != null) {
      setStatus(PENDING_STATUS);
    }

    if (StringUtils.isEmpty(sortQuery)) {
      sortQuery = DEFAULT_SORT_ORDER;
    }

    sort = new Sort(getSortOrderingList());
  }

  private List<Order> getSortOrderingList() {
    List<Order> orders = new ArrayList<>();
    for (String sortParam : Arrays.asList(sortQuery.split(COMMA))) {
      int colonIndex = sortParam.indexOf(COLON);
      String direction = sortParam.substring(colonIndex + 1, sortParam.length());
      if (direction.equalsIgnoreCase(ASCENDING)) {
        orders.add(new Order(Direction.ASC, sortParam.substring(0, colonIndex)));
      } else if (direction.equalsIgnoreCase(DESCENDING)) {
        orders.add(new Order(Direction.DESC, sortParam.substring(0, colonIndex)));
      } else {
        throw new BaseException(Response.SC_BAD_REQUEST, INCORRECT_SORT_PARAM);
      }
    }
    return orders;
  }
}
