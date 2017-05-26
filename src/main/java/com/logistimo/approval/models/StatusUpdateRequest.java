package com.logistimo.approval.models;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 12/05/17.
 */

@Data
public class StatusUpdateRequest {

  @NotNull
  private String status;

  @NotNull
  private String updatedBy;

  private String messageId;
}
