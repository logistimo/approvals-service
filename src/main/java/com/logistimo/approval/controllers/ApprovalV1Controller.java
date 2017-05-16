package com.logistimo.approval.controllers;

import com.logistimo.approval.actions.CreateApprovalAction;
import com.logistimo.approval.actions.GetApprovalAction;
import com.logistimo.approval.actions.GetApprovalStatusHistoryAction;
import com.logistimo.approval.actions.UpdateApprovalDomainMappingAction;
import com.logistimo.approval.actions.UpdateApprovalStatusAction;
import com.logistimo.approval.models.ApprovalRequest;
import com.logistimo.approval.models.ApprovalResponse;
import com.logistimo.approval.models.DomainUpdateRequest;
import com.logistimo.approval.models.StatusUpdateRequest;
import com.logistimo.approval.models.StatusResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by nitisha.khandelwal on 08/05/17.
 */

@RestController
@EnableAutoConfiguration
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "/v1/approvals")
public class ApprovalV1Controller {

  private GetApprovalAction getApprovalAction;
  private CreateApprovalAction createApprovalAction;
  private GetApprovalStatusHistoryAction getApprovalStatusHistoryAction;
  private UpdateApprovalStatusAction updateApprovalStatusAction;
  private UpdateApprovalDomainMappingAction updateApprovalDomainMappingAction;

  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  @RequestMapping(path = "/{approvalId}", method = RequestMethod.GET)
  public ApprovalResponse getApproval(@PathVariable("approvalId") String approvalId) {
    return getApprovalAction.invoke(approvalId);
  }

  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  @RequestMapping(path = "/", method = RequestMethod.GET)
  public List<ApprovalResponse> getApprovals(@RequestParam("requester_id") String requesterId,
      @RequestParam("status") String status, @RequestParam("expiring_in") String expiringIn,
      @RequestParam("approver_id") String approverId,
      @RequestParam("approver_status") String approverStatus,
      @RequestParam("type") String type, @RequestParam("type_id") String typeId,
      @RequestParam("ordered_by") String orderedBy,
      @RequestParam("attribute_key") String attributeKey,
      @RequestParam("attribute_value") String attributeValue) {
    return null;
  }

  @ResponseBody
  @ResponseStatus(value = HttpStatus.CREATED)
  @RequestMapping(path = "/", method = RequestMethod.POST)
  public ApprovalResponse createApproval(@RequestBody ApprovalRequest approval) {
    return createApprovalAction.invoke(approval);
  }

  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  @RequestMapping(path = "/{approvalId}/status_history", method = RequestMethod.GET)
  public List<StatusResponse> getApprovalStatusHistory(
      @PathVariable("approvalId") String approvalId) {
    return getApprovalStatusHistoryAction.invoke(approvalId);
  }


  @ResponseBody
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  @RequestMapping(path = "/{approvalId}/status", method = RequestMethod.PUT)
  public Void updateApprovalStatusHistory(@PathVariable("approvalId") String approvalId,
      @RequestBody StatusUpdateRequest request) {
    return updateApprovalStatusAction.invoke(approvalId, request);
  }

  @ResponseBody
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  @RequestMapping(path = "/{approvalId}/domains", method = RequestMethod.PUT)
  public Void updateApprovalDomainMapping(@PathVariable("approvalId") String approvalId,
      @RequestBody DomainUpdateRequest request) {
    return updateApprovalDomainMappingAction.invoke(approvalId, request);
  }
}
