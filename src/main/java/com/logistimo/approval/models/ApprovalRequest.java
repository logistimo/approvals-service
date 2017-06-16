package com.logistimo.approval.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.logistimo.approval.exception.BaseException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.apache.catalina.connector.Response;
import org.springframework.util.CollectionUtils;

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

  @NotNull(message = "Source Domain Id Id cannot be null.")
  private Long sourceDomainId;

  private List<Long> domains;

  private String message;

  private Map<String, String> attributes;

  private List<ApproverRequest> approvers;
}
