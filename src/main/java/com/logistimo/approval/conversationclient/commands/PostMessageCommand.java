package com.logistimo.approval.conversationclient.commands;

import com.logistimo.approval.conversationclient.config.ConversationClientConfig;
import com.logistimo.approval.conversationclient.request.PostMessageResponse;
import com.logistimo.approval.conversationclient.response.PostMessageRequest;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.web.client.RestTemplate;

/**
 * Created by nitisha.khandelwal on 15/05/17.
 */

public class PostMessageCommand extends HystrixCommand<PostMessageResponse> {

  private String type;

  private String typeId;

  private PostMessageRequest request;

  private RestTemplate restTemplate;

  private ConversationClientConfig config;

  private static final String PATH = "/s2/api/conversation/add_message/%s/%s";
  private static final String GROUP_KEY = "Conversation-Service";

  public PostMessageCommand(RestTemplate restTemplate, ConversationClientConfig config,
      PostMessageRequest request, String type, String typeId) {
    super(HystrixCommandGroupKey.Factory.asKey(GROUP_KEY), 60000);
    this.restTemplate = restTemplate;
    this.config = config;
    this.request = request;
    this.type = type;
    this.typeId = typeId;
  }

  @Override
  protected PostMessageResponse run() throws Exception {

    return restTemplate.postForObject(config.getUrl() + String.format(PATH, type, typeId), request,
        PostMessageResponse.class);
  }

}
