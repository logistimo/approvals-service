package com.logistimo.approval.actions;

import static com.logistimo.approval.utils.Utility.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApprovalStatusHistoryRepository;
import com.logistimo.approval.utils.JmsUtil;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
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
  private IApprovalStatusHistoryRepository approvalStatusHistoryRepository;


  @Mock
  private JmsUtil jmsUtil;

  @InjectMocks
  private UpdateApprovalStatusAction action;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(approvalRepository.findOne(APPROVAL_ID)).thenReturn(getApproval());
    when(approvalStatusHistoryRepository.findLastUpdateByApprovalId(APPROVAL_ID))
        .thenReturn(getLastStatus());
  }

  @Test
  public void updateApprovalStatusActionTest() {

  }

  @Test(expected = BaseException.class)
  public void updateApprovalStatusActionThrowsExceptionTest() throws IOException {
    action.invoke("A456", getStatusUpdateRequest());
    verify(approvalRepository, times(1)).findOne(APPROVAL_ID);
  }
}
