package com.logistimo.approval.models;

import javax.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Created by nitisha.khandelwal on 12/05/17.
 */

@Getter
public class StatusUpdateRequest {

  @NotNull
  private String status;

  @NotNull
  private String updatedBy;

  private String messageId;
}
