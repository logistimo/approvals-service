package com.logistimo.approval.repository;

import com.logistimo.approval.entity.ApprovalAttributes;
import com.logistimo.approval.entity.ApproverQueue;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

public interface IApprovalAttributesRepository extends CrudRepository<ApprovalAttributes, Long> {

  @Query(value = "SELECT * FROM approval_attributes WHERE approval_id = ?1", nativeQuery = true)
  List<ApprovalAttributes> findByApprovalId(String approvalId);

}
