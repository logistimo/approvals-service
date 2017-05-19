package com.logistimo.approval.config;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by nitisha.khandelwal on 16/05/17.
 */

@Component
public class CamelRoutes extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    from("jms:approval-approverStatus-update").to("log:?level=TRACE&showAll=true");
  }
}
