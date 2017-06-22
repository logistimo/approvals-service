package com.logistimo.approval.models;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 22/05/17.
 */

@Data
public class ApprovalStatusUpdateEvent implements Serializable {

  public ApprovalStatusUpdateEvent(String approvalId, String type, String typeId,
      String status, String updatedBy, Date updatedAt) {
    this.approvalId = approvalId;
    this.type = type;
    this.typeId = typeId;
    this.status = status;
    this.updatedBy = updatedBy;
    this.updatedAt = updatedAt;
  }

  @NotNull
  private String approvalId;

  @NotNull
  private String type;

  @NotNull
  private String typeId;

  @NotNull
  private String status;

  @NotNull
  private String updatedBy;

  @NotNull
  private Date updatedAt;
}
