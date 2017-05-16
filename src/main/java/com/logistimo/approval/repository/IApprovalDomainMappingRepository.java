package com.logistimo.approval.repository;

import com.logistimo.approval.entity.ApprovalDomainMapping;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

public interface IApprovalDomainMappingRepository extends
    CrudRepository<ApprovalDomainMapping, Long> {

  @Query(value = "SELECT * FROM approval_domain_mapping WHERE approval_id = ?1", nativeQuery = true)
  List<ApprovalDomainMapping> findByApprovalId(String approvalId);

  @Query(value = "DELETE FROM approval_domain_mapping WHERE approval_id = ?1", nativeQuery = true)
  void deleteByApprovalId(String approvalId);
}
