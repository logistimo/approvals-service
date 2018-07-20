package com.logistimo.approval.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 03/07/17.
 */

@Data
public class ApproverStatusUpdateEvent implements Serializable {

  public ApproverStatusUpdateEvent(String approvalId, String type, String typeId,
      String requesterId, Date requestedAt, Date expiryTime, int expiryInHours,
      List<String> nextApproverIds, String userId, String status) {
    this.approvalId = approvalId;
    this.type = type;
    this.typeId = typeId;
    this.requesterId = requesterId;
    this.requestedAt = requestedAt;
    this.expiryTime = expiryTime;
    this.expiryInHours = expiryInHours;
    this.nextApproverIds = nextApproverIds;
    this.userId = userId;
    this.status = status;
  }

  @NotNull
  private String approvalId;

  @NotNull
  private String type;

  @NotNull
  private String typeId;

  @NotNull
  private String requesterId;

  @NotNull
  private Date requestedAt;

  private Date expiryTime;

  private int expiryInHours;

  private List<String> nextApproverIds;

  @NotNull
  private String userId;

  @NotNull
  private String status;
}
