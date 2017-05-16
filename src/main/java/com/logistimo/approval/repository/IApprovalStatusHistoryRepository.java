package com.logistimo.approval.repository;

import com.logistimo.approval.entity.ApprovalStatusHistory;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

public interface IApprovalStatusHistoryRepository extends
    CrudRepository<ApprovalStatusHistory, Long> {

  @Query(value = "SELECT * FROM approval_status_history  WHERE approval_id = ?1", nativeQuery = true)
  List<ApprovalStatusHistory> findByApprovalId(String approvalId);

  @Query(value = "SELECT * FROM approval_status_history  WHERE approval_id = ?1 ORDER BY start_time DESC LIMIT 1", nativeQuery = true)
  ApprovalStatusHistory findLastUpdateByApprovalId(String approvalId);

}
