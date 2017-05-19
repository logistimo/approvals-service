package com.logistimo.approval.conversationclient.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by nitisha.khandelwal on 15/05/17.
 */

@Getter
@Component
public class ConversationClientConfig {

  @Value("${conversationservice.url}")
  private String url;

}
