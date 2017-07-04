package com.logistimo.approval.models;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 03/07/17.
 */

@Data
public class ApproverStatusUpdateEvent implements Serializable {

  public ApproverStatusUpdateEvent(String approvalId, String userId, String status) {
    this.approvalId = approvalId;
    this.userId = userId;
    this.status = status;

  }

  @NotNull
  private String approvalId;

  @NotNull
  private String userId;

  @NotNull
  private String status;
}
