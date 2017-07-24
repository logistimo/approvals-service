package com.logistimo.approval.controllers;

import com.logistimo.approval.actions.CreateApprovalAction;
import com.logistimo.approval.actions.GetApprovalAction;
import com.logistimo.approval.actions.GetApprovalStatusHistoryAction;
import com.logistimo.approval.actions.GetFilteredApprovalsAction;
import com.logistimo.approval.actions.UpdateApprovalDomainMappingAction;
import com.logistimo.approval.actions.UpdateApprovalStatusAction;
import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.models.ApprovalFilters;
import com.logistimo.approval.models.ApprovalRequest;
import com.logistimo.approval.models.ApprovalResponse;
import com.logistimo.approval.models.DomainUpdateRequest;
import com.logistimo.approval.models.StatusResponse;
import com.logistimo.approval.models.StatusUpdateRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
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

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

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
  private GetFilteredApprovalsAction getFilteredApprovalsAction;
  private UpdateApprovalStatusAction updateApprovalStatusAction;
  private UpdateApprovalDomainMappingAction updateApprovalDomainMappingAction;

  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  @RequestMapping(path = "/{approvalId}", method = RequestMethod.GET)
  public ApprovalResponse getApproval(@PathVariable("approvalId") String approvalId) {
    return getApprovalAction.invoke(approvalId);
  }

  @ResponseBody
  @ResponseStatus(value = HttpStatus.CREATED)
  @RequestMapping(method = RequestMethod.POST)
  public ApprovalResponse createApproval(@Valid @RequestBody ApprovalRequest request) {
    return createApprovalAction.invoke(request);
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
      @Valid @RequestBody StatusUpdateRequest request) {
    return updateApprovalStatusAction.invoke(approvalId, request);
  }

  @ResponseBody
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  @RequestMapping(path = "/{approvalId}/domains", method = RequestMethod.PUT)
  public Void updateApprovalDomainMapping(@PathVariable("approvalId") String approvalId,
      @RequestBody DomainUpdateRequest request) {
    return updateApprovalDomainMappingAction.invoke(approvalId, request);
  }

  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  @RequestMapping(method = RequestMethod.GET)
  public Page<Approval> getApprovals(@RequestParam(value = "offset") int offset,
      @RequestParam(value = "size") int size,
      @RequestParam(value = "type", required = false) String type,
      @RequestParam(value = "type_id", required = false) String typeId,
      @RequestParam(value = "status", required = false) String status,
      @RequestParam(value = "expiring_in", required = false) Integer expiringInMinutes,
      @RequestParam(value = "requester_id", required = false) String requesterId,
      @RequestParam(value = "approver_id", required = false) String approverId,
      @RequestParam(value = "approver_status", required = false) String approverStatus,
      @RequestParam(value = "attribute_key", required = false) String attributeKey,
                                     @RequestParam(value = "attribute_value", required = false) String[] attributeValues,
                                     @RequestParam(value = "domain_id") Long domainId,
      @RequestParam(value = "sort", required = false) String sortQuery) {
    ApprovalFilters filters = getApprovalFilters(offset, size, requesterId, status,
        expiringInMinutes, approverId, approverStatus, type, typeId, sortQuery, attributeKey,
        attributeValues, domainId);
    return getFilteredApprovalsAction.invoke(filters);
  }

  private ApprovalFilters getApprovalFilters(int offset, int size, String requesterId,
      String status, Integer expiringInMinutes, String approverId, String approverStatus,
                                             String type, String typeId, String sortQuery,
                                             String attributeKey, String[] attributeValues,
                                             Long domainId) {
    ApprovalFilters filters = new ApprovalFilters();
    filters.setOffset(offset);
    filters.setSize(size);
    filters.setRequesterId(requesterId);
    filters.setStatus(status);
    filters.setExpiringInMinutes(expiringInMinutes);
    filters.setApproverId(approverId);
    filters.setApproverStatus(approverStatus);
    filters.setType(type);
    filters.setTypeId(typeId);
    filters.setSortQuery(sortQuery);
    filters.setAttributeKey(attributeKey);
    if (attributeValues != null) {
      filters.setAttributeValues(Arrays.asList(attributeValues));
    }
    filters.setDomainId(domainId);
    filters.validate();
    return filters;
  }
}
