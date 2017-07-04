package com.logistimo.approval.utils;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

/**
 * Created by nitisha.khandelwal on 03/07/17.
 */

@Component
public class ApproverStatusUpdateRoute extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    from("seda:approver-status-update")
        .marshal()
        .json(JsonLibrary.Gson)
        .to("activemq:approver-status-update");
  }
}
