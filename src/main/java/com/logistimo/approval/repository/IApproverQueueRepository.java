package com.logistimo.approval.repository;

import com.logistimo.approval.entity.ApproverQueue;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

public interface IApproverQueueRepository extends CrudRepository<ApproverQueue, Long> {

  @Query(value = "SELECT * FROM approver_queue WHERE approval_id = ?1 ORDER BY start_time", nativeQuery = true)
  List<ApproverQueue> findByApprovalId(String approvalId);

  @Query(value = "SELECT * FROM approver_queue WHERE approval_id = ?1 AND queue_id = ?2 ORDER BY start_time", nativeQuery = true)
  List<ApproverQueue> findByApprovalIdAndQueueId(String approvalId, Long queueId);
}
