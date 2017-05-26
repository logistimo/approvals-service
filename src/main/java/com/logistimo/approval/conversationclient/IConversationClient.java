package com.logistimo.approval.conversationclient;

import com.logistimo.approval.conversationclient.request.PostMessageResponse;
import com.logistimo.approval.conversationclient.response.PostMessageRequest;

/**
 * Created by nitisha.khandelwal on 15/05/17.
 */

public interface IConversationClient {

  PostMessageResponse postMessage(PostMessageRequest request, String type, String typeId);
}
