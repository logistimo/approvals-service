package com.logistimo.approval.controllers;

import com.logistimo.approval.actions.GetFilteredApprovalsAction;
import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.models.ApprovalFilters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Created by charan on 24/07/17.
 */
@RestController
@EnableAutoConfiguration
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "/v1/approvals-search")
public class ApprovalsSearchV1Controller {

  private GetFilteredApprovalsAction getFilteredApprovalsAction;

  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  @RequestMapping(method = RequestMethod.POST)
  public Page<Approval> getApprovals(@RequestBody ApprovalFilters approvalFilters) {
    approvalFilters.validate();
    return getFilteredApprovalsAction.invoke(approvalFilters);
  }

}
