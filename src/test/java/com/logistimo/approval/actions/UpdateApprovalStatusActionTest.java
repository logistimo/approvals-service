package com.logistimo.approval.actions;

import static com.logistimo.approval.utils.TestUtility.*;
import static com.logistimo.approval.utils.TestUtility.getApproval;
import static com.logistimo.approval.utils.TestUtility.getStatusUpdateRequest;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static junit.framework.TestCase.assertEquals;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalStatusHistory;
import com.logistimo.approval.entity.ApproverQueue;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.ApprovalStatusUpdateEvent;
import com.logistimo.approval.models.StatusUpdateRequest;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApprovalStatusHistoryRepository;
import com.logistimo.approval.utils.Constants;
import com.logistimo.approval.utils.Utility;
import java.io.IOException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by nitisha.khandelwal on 23/05/17.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class UpdateApprovalStatusActionTest {

  @Mock
  private IApprovalRepository approvalRepository;

  @Mock
  private IApprovalStatusHistoryRepository statusHistoryRepository;

  @Mock
  private Utility utility;

  @InjectMocks
  private UpdateApprovalStatusAction action;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(statusHistoryRepository.findLastUpdateByApprovalId(APPROVAL_ID))
        .thenReturn(getLastStatus());
    when(statusHistoryRepository.save(any(ApprovalStatusHistory.class)))
        .thenReturn(getCurrentStatus());
  }

  @Test
  public void updateApprovalStatusActionTest() {
    when(approvalRepository.findOne(APPROVAL_ID)).thenReturn(getApproval());
    StatusUpdateRequest request = getStatusUpdateRequest();
    action.invoke(APPROVAL_ID, request);

    final ArgumentCaptor<ApprovalStatusUpdateEvent> eventCaptor = ArgumentCaptor
        .forClass(ApprovalStatusUpdateEvent.class);

    verify(approvalRepository, times(1)).findOne(APPROVAL_ID);
    verify(approvalRepository, times(1)).save(any(Approval.class));
    verify(statusHistoryRepository, times(2)).save(any(ApprovalStatusHistory.class));
    verify(utility, times(1)).publishApprovalStatusUpdateEvent(eventCaptor.capture());
    verify(utility, times(1)).addMessageToConversation(anyString(), anyString(), anyString(), any());

    assertEquals(eventCaptor.getValue().getStatus(), request.getStatus());
    assertEquals(eventCaptor.getValue().getUpdatedBy(), request.getUpdatedBy());
    assertEquals(eventCaptor.getValue().getApprovalId(), APPROVAL_ID);
    assertEquals(eventCaptor.getValue().getType(), getApproval().getType());
    assertEquals(eventCaptor.getValue().getTypeId(), getApproval().getTypeId());
  }

  @Test(expected = BaseException.class)
  public void invalidStateTransitionTest() {
    Approval approval = getApproval();
    approval.setStatus("AP");
    when(approvalRepository.findOne(APPROVAL_ID)).thenReturn(approval);
    try {
      action.invoke(APPROVAL_ID, getStatusUpdateRequest());
    } catch (BaseException e) {
      verify(approvalRepository, times(1)).findOne(APPROVAL_ID);
      assertEquals(e.getMessage(), Constants.APPROVAL_NOT_PENDING);
      throw e;
    }
  }

  @Test(expected = BaseException.class)
  public void messageIdRequiredTest() {
    when(approvalRepository.findOne(APPROVAL_ID)).thenReturn(getApproval());
    StatusUpdateRequest request = getStatusUpdateRequest();
    request.setStatus("RJ");
    request.setMessage(null);
    try {
      action.invoke(APPROVAL_ID, request);
    } catch (BaseException e) {
      verify(approvalRepository, times(1)).findOne(APPROVAL_ID);
      assertEquals(e.getMessage(), Constants.MESSAGE_ID_REQUIRED);
      throw e;
    }
  }

  @Test(expected = BaseException.class)
  public void updatedByRequesterIdTest() {
    when(approvalRepository.findOne(APPROVAL_ID)).thenReturn(getApproval());
    StatusUpdateRequest request = getStatusUpdateRequest();
    request.setUpdatedBy("R001");
    try {
      action.invoke(APPROVAL_ID, request);
    } catch (BaseException e) {
      verify(approvalRepository, times(1)).findOne(APPROVAL_ID);
      assertEquals(e.getMessage(), Constants.UPDATED_BY_REQUSTER_ID);
      throw e;
    }
  }

  @Test(expected = BaseException.class)
  public void requesterIdNotActiveTest() {
    Approval approval = getApproval();
    approval.setApprovers(Collections.singleton(new ApproverQueue(APPROVAL_ID, "U001", "QD",
        "PRIMARY", 1L, null, null)));
    when(approvalRepository.findOne(APPROVAL_ID)).thenReturn(approval);
    try {
      action.invoke(APPROVAL_ID, getStatusUpdateRequest());
    } catch (BaseException e) {
      verify(approvalRepository, times(1)).findOne(APPROVAL_ID);
      assertEquals(e.getMessage(), Constants.REQUESTER_ID_NOT_ACTIVE);
      throw e;
    }
  }

  @Test(expected = BaseException.class)
  public void requesterIdNotPresentTest() {
    when(approvalRepository.findOne(APPROVAL_ID)).thenReturn(getApproval());
    StatusUpdateRequest request = getStatusUpdateRequest();
    request.setUpdatedBy("U999");
    try {
      action.invoke(APPROVAL_ID, request);
    } catch (BaseException e) {
      verify(approvalRepository, times(1)).findOne(APPROVAL_ID);
      assertEquals(e.getMessage(), Constants.REQUESTER_NOT_PRESENT);
      throw e;
    }
  }

  @Test(expected = BaseException.class)
  public void requesterIdCannotBeApproverTest() {
    when(approvalRepository.findOne(APPROVAL_ID)).thenReturn(getApproval());
    StatusUpdateRequest request = getStatusUpdateRequest();
    request.setStatus("CN");
    try {
      action.invoke(APPROVAL_ID, request);
    } catch (BaseException e) {
      verify(approvalRepository, times(1)).findOne(APPROVAL_ID);
      assertEquals(e.getMessage(), Constants.REQUESTER_ID_CANNOT_BE_AN_APPROVER);
      throw e;
    }
  }

  @Test(expected = BaseException.class)
  public void approvalNotFound() throws IOException {
    action.invoke("A456", getStatusUpdateRequest());
    verify(approvalRepository, times(1)).findOne("A456");
  }
}
