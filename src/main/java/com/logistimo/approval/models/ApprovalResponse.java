package com.logistimo.approval.models;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ApprovalResponse {

  private String approvalId;

  private String type;

  private String typeId;

  private String requesterId;

  private String status;

  private String conversationId;

  private Long sourceDomainId;

  private List<Long> domains = new ArrayList<>();

  @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private Date expireAt;

  private Map<String, String> attributes = new HashMap<>();

  private List<Approver> approvers = new ArrayList<>();

  @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private Date createdAt;

  @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private Date updatedAt;

}
