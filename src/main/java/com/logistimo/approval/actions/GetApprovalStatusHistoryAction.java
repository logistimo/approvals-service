package com.logistimo.approval.actions;

import com.logistimo.approval.entity.ApprovalStatusHistory;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.StatusResponse;
import com.logistimo.approval.repository.IApprovalStatusHistoryRepository;
import java.util.ArrayList;
import java.util.List;
import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Created by nitisha.khandelwal on 12/05/17.
 */

@Component
public class GetApprovalStatusHistoryAction {

  public static final String STATUS_HISTORY_NOT_FOUND = "Status History not found for the approval with the id - ";

  @Autowired
  private IApprovalStatusHistoryRepository statusHistoryRepository;

  public List<StatusResponse> invoke(String approvalId) {

    List<ApprovalStatusHistory> statusHistories = statusHistoryRepository.findByApprovalId(approvalId);

    if (CollectionUtils.isEmpty(statusHistories)) {
      throw new BaseException(Response.SC_NOT_FOUND, STATUS_HISTORY_NOT_FOUND + approvalId);
    }

    ModelMapper mapper = new ModelMapper();
    List<StatusResponse> response = new ArrayList<>();

    for (ApprovalStatusHistory statusHistory : statusHistories) {
      response.add(mapper.map(statusHistory, StatusResponse.class));
    }

    return response;
  }
}
