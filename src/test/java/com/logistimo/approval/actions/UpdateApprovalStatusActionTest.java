package com.logistimo.approval.actions;

import static com.logistimo.approval.utils.Utility.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static junit.framework.TestCase.assertEquals;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalStatusHistory;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.ApprovalStatusUpdateEvent;
import com.logistimo.approval.models.StatusUpdateRequest;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApprovalStatusHistoryRepository;
import com.logistimo.approval.utils.JmsUtil;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by nitisha.khandelwal on 23/05/17.
 */

public class UpdateApprovalStatusActionTest {

  @Mock
  private IApprovalRepository approvalRepository;

  @Mock
  private IApprovalStatusHistoryRepository statusHistoryRepository;

  @Mock
  private JmsUtil jmsUtil;

  @InjectMocks
  private UpdateApprovalStatusAction action;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(approvalRepository.findOne(APPROVAL_ID)).thenReturn(getApproval());
    when(statusHistoryRepository.findLastUpdateByApprovalId(APPROVAL_ID))
        .thenReturn(getLastStatus());
  }

  @Test
  public void updateApprovalStatusActionTest() {

    StatusUpdateRequest request = getStatusUpdateRequest();
    action.invoke(APPROVAL_ID, request);

    final ArgumentCaptor<ApprovalStatusUpdateEvent> eventCaptor = ArgumentCaptor
        .forClass(ApprovalStatusUpdateEvent.class);

    verify(approvalRepository, times(1)).findOne(APPROVAL_ID);
    verify(approvalRepository, times(1)).save(any(Approval.class));
    verify(statusHistoryRepository, times(2)).save(any(ApprovalStatusHistory.class));
    verify(jmsUtil, times(1)).sendMessage(eventCaptor.capture());

    assertEquals(eventCaptor.getValue().getStatus(), request.getStatus());
    assertEquals(eventCaptor.getValue().getUpdatedBy(), request.getUpdatedBy());
    assertEquals(eventCaptor.getValue().getApprovalId(), APPROVAL_ID);
    assertEquals(eventCaptor.getValue().getType(), getApproval().getType());
    assertEquals(eventCaptor.getValue().getTypeId(), getApproval().getTypeId());

  }

  @Test(expected = BaseException.class)
  public void approvalNotFound() throws IOException {
    action.invoke("A456", getStatusUpdateRequest());
    verify(approvalRepository, times(1)).findOne(APPROVAL_ID);
  }
}
