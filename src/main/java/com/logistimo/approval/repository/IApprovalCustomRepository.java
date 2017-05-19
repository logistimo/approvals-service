package com.logistimo.approval.repository;

import com.logistimo.approval.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by nitisha.khandelwal on 18/05/17.
 */

public interface IApprovalCustomRepository extends JpaRepository<Approval, String>,
    JpaSpecificationExecutor<Approval> {

}
