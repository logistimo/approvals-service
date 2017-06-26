package com.logistimo.approval.utils;

import com.logistimo.approval.conversationclient.IConversationClient;
import com.logistimo.approval.conversationclient.request.PostMessageResponse;
import com.logistimo.approval.conversationclient.response.PostMessageRequest;
import com.logistimo.approval.models.ApprovalStatusUpdateEvent;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by nitisha.khandelwal on 16/05/17.
 */

@Component
public class Utility {

  @Autowired
  @Produce(uri = "seda:approval-status-update")
  ProducerTemplate producerTemplate;

  @Autowired
  private IConversationClient conversationClient;

  public void publishStatusUpdateEvent(ApprovalStatusUpdateEvent message) {
    producerTemplate.sendBody(message);
  }

  public PostMessageResponse addMessageToConversation(String approvalId, String message,
      String userId, Long domainId) {
    PostMessageRequest postMessageRequest = new PostMessageRequest(message, userId, domainId);
    return conversationClient.postMessage(postMessageRequest, "APPROVAL", approvalId);
  }
}
