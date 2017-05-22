package com.logistimo.approval.models;

import javax.validation.constraints.NotNull;
import lombok.Setter;

/**
 * Created by nitisha.khandelwal on 22/05/17.
 */

@Setter
public class ApprovalStatusUpdateMessage {

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
}
