package com.logistimo.approval.models;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 12/05/17.
 */

@Data
public class StatusUpdateRequest {

  @NotNull(message = "Status cannot be null.")
  private String status;

  @NotNull(message = "Updated By cannot be null.")
  private String updatedBy;

  private String message;
}
