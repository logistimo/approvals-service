package com.logistimo.approval.conversationclient.internal;

import com.logistimo.approval.conversationclient.IConversationClient;
import com.logistimo.approval.conversationclient.commands.PostMessageCommand;
import com.logistimo.approval.conversationclient.config.ConversationClientConfig;
import com.logistimo.approval.conversationclient.request.PostMessageResponse;
import com.logistimo.approval.conversationclient.response.PostMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by nitisha.khandelwal on 15/05/17.
 */

@Service
public class ConversationClient implements IConversationClient {

  @Autowired
  ConversationClientConfig config;

  private final RestTemplate restTemplate;

  protected ConversationClient(RestTemplateBuilder restTemplateBuilder) {
    restTemplate = restTemplateBuilder.build();
  }

  @Override
  public PostMessageResponse postMessage(PostMessageRequest request, String type, String typeId) {

    PostMessageCommand command = new PostMessageCommand(restTemplate, config, request, type, typeId);
    return command.execute();
  }
}
