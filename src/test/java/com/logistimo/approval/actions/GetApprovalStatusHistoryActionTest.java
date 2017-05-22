package com.logistimo.approval.actions;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

import com.logistimo.approval.entity.ApprovalStatusHistory;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.StatusResponse;
import com.logistimo.approval.repository.IApprovalStatusHistoryRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by nitisha.khandelwal on 22/05/17.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class GetApprovalStatusHistoryActionTest {

  @Mock
  private IApprovalStatusHistoryRepository statusHistoryRepository;

  @InjectMocks
  private GetApprovalStatusHistoryAction action;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(statusHistoryRepository.findByApprovalId("A123")).thenReturn(getApprovalStatusHistories());
  }

  @Test
  public void getApprovalTest() {
    List<StatusResponse> response = action.invoke("A123");
    assertEquals(response.size(), 2);
  }

  @Test(expected = BaseException.class)
  public void getApprovalStatusHistoryThrowsExceptionTest() {
    action.invoke("A456");
  }

  private List<ApprovalStatusHistory> getApprovalStatusHistories() {
    List<ApprovalStatusHistory> statusHistories = new ArrayList<>();
    ApprovalStatusHistory history1 = new ApprovalStatusHistory();
    history1.setApprovalId("A123");
    history1.setStatus("PENDING");
    history1.setUpdatedBy("U001");
    statusHistories.add(history1);
    ApprovalStatusHistory history2 = new ApprovalStatusHistory();
    history1.setApprovalId("A123");
    history1.setStatus("APPROVED");
    history1.setUpdatedBy("U002");
    statusHistories.add(history2);
    return statusHistories;
  }
}
