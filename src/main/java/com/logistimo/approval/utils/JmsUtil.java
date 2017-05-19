package com.logistimo.approval.utils;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by nitisha.khandelwal on 16/05/17.
 */

@Component
public class JmsUtil {

  @Autowired
  @Produce(uri = "jms:approval-status-update")
  ProducerTemplate producerTemplate;

  public void sendMessage(String message) {
    producerTemplate.sendBody(message);
  }

}
