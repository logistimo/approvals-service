package com.logistimo.approval.utils;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

/**
 * Created by nitisha.khandelwal on 21/06/17.
 */

@Component
public class ApprovalStatusUpdateRoute extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    from("seda:approval-status-update")
        .marshal()
        .json(JsonLibrary.Gson)
        .to("activemq:approval-status-update");
  }
}
