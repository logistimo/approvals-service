package com.logistimo.approval.utils;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

/**
 * Created by charan on 14/07/17.
 */
@Component
public class ApprovalCreationRoute extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    from("seda:approval-requested")
        .marshal()
        .json(JsonLibrary.Gson)
        .delay(5000)
        .to("activemq:approval-requested");
  }
}