package com.logistimo.approval.models;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

@Data
public class ApproverRequest {

  @NotNull(message = "User Ids cannot be null.")
  private List<String> userIds;

  @NotNull(message = "Approver Expiry cannot be null.")
  private int expiry;

  @NotNull(message = "Approver Type cannot be null.")
  private String type;
}
