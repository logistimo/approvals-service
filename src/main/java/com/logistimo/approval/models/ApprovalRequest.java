package com.logistimo.approval.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApprovalRequest {

  @NotNull(message = "Type cannot be null.")
  private String type;

  @NotNull(message = "Type Id cannot be null.")
  private String typeId;

  @NotNull(message = "Requester Id cannot be null.")
  private String requesterId;

  @NotNull(message = "Source Domain Id cannot be null.")
  private Long sourceDomainId;

  private List<Long> domains;

  private String message;

  private Map<String, String> attributes;

  private List<ApproverRequest> approvers;
}
