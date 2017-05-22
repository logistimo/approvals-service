package com.logistimo.approval.conversationclient.config;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Created by nitisha.khandelwal on 19/05/17.
 */

public class XConversationClientInterceptor implements ClientHttpRequestInterceptor {

  private static final String HEADER_NAME = "app-name";
  private static final String HEADER_VALUE = "approvals";

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {
    HttpHeaders headers = request.getHeaders();
    headers.add(HEADER_NAME, HEADER_VALUE);
    return execution.execute(request, body);
  }
}
