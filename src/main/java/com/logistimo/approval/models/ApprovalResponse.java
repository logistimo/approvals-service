package com.logistimo.approval.models;

import java.util.Date;
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

  private List<Long> domains;

  private String message;

  private Date expireAt;

  private Map<String, String> attributes;

  private List<Approver> approvers;

  private Date createdAt;

  private Date updatedAt;

}
