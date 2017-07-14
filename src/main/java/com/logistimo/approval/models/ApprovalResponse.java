package com.logistimo.approval.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 11/05/17.
 */

@Data
@JsonInclude(Include.NON_EMPTY)
public class ApprovalResponse {

  private String approvalId;

  private String type;

  private String typeId;

  private String requesterId;

  private String status;

  private String conversationId;

  private Long sourceDomainId;

  private List<Long> domains = new ArrayList<>();

  private Date expireAt;

  private Map<String, String> attributes = new HashMap<>();

  private String activeApproverType;

  private List<ApproverResponse> approvers = new ArrayList<>();

  private Date createdAt;

  private Date updatedAt;

  private String updatedBy;

}
