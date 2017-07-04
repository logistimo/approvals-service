package com.logistimo.approval.utils;

import static com.logistimo.approval.utils.Constants.CONVERSATION_TYPE;

import com.logistimo.approval.conversationclient.IConversationClient;
import com.logistimo.approval.conversationclient.request.PostMessageResponse;
import com.logistimo.approval.conversationclient.response.PostMessageRequest;
import com.logistimo.approval.models.ApprovalStatusUpdateEvent;
import com.logistimo.approval.models.ApproverStatusUpdateEvent;
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
  ProducerTemplate approvalStatusProducerTemplate;

  @Autowired
  @Produce(uri = "seda:approver-status-update")
  ProducerTemplate approverStatusProducerTemplate;

  @Autowired
  private IConversationClient conversationClient;

  public void publishApprovalStatusUpdateEvent(ApprovalStatusUpdateEvent message) {
    approvalStatusProducerTemplate.sendBody(message);
  }

  public void publishApproverStatusUpdateEvent(ApproverStatusUpdateEvent message) {
    approverStatusProducerTemplate.sendBody(message);
  }

  public PostMessageResponse addMessageToConversation(String approvalId, String message,
      String userId, Long domainId) {
    PostMessageRequest postMessageRequest = new PostMessageRequest(message, userId, domainId);
    return conversationClient.postMessage(postMessageRequest, CONVERSATION_TYPE, approvalId);
  }
}
