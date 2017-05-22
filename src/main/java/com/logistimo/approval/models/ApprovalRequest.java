package com.logistimo.approval.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApprovalRequest {

  @NotNull
  private String type;

  @NotNull
  private String typeId;

  @NotNull
  private String requesterId;

  private Long sourceDomainId;

  private List<Long> domains;

  private String message;

  @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private Date expireAt;

  private Map<String, String> attributes;

  private List<Approver> approvers;

}
