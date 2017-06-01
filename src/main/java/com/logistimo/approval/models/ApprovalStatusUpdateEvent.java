package com.logistimo.approval.models;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 22/05/17.
 */

@Data
public class ApprovalStatusUpdateEvent implements Serializable {

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
