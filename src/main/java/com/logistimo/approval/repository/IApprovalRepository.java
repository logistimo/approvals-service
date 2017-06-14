package com.logistimo.approval.repository;

import com.logistimo.approval.entity.Approval;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by nitisha.khandelwal on 09/05/17.
 */

public interface IApprovalRepository extends CrudRepository<Approval, String> {

  @Query(value = "SELECT * FROM approvals WHERE type = ?1 AND type_id = ?2 AND status IN('PENDING', 'APPROVED')", nativeQuery = true)
  List<Approval> findApprovedOrPendingApprovalsByTypeAndTypeId(String type, String typeId);
}
