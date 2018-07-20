package com.logistimo.approval.repository;

import com.logistimo.approval.entity.Task;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by nitisha.khandelwal on 30/06/17.
 */

public interface ITaskRepository extends CrudRepository<Task, Long> {

  @Query(value = "SELECT * FROM tasks WHERE status = 'qd' and run_time <= ?1", nativeQuery = true)
  List<Task> findPendingTasks(Date now);

  @Query(value = "SELECT * FROM tasks WHERE status = 'qd'  and approval_id = ?1", nativeQuery = true)
  List<Task> findPendingTasksByApprovalId(String approvalId);
}
