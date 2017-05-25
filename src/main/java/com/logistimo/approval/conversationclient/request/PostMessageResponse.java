package com.logistimo.approval.conversationclient.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 15/05/17.
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostMessageResponse {

  @JsonProperty("conversationId")
  private String conversationId;


  @JsonProperty("messageId")
  private String messageId;
}
