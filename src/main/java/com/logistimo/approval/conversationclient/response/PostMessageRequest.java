package com.logistimo.approval.conversationclient.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 15/05/17.
 */

@Data
public class PostMessageRequest {

  @JsonProperty("data")
  private String data;

  @JsonProperty("userId")
  private String userId;

  @JsonProperty("domainId")
  private Long domainId;

  public PostMessageRequest(String data, String userId, Long domainId) {
    this.data = data;
    this.userId = userId;
    this.domainId = domainId;
  }
}
