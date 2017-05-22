package com.logistimo.approval.config;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalAttributes;
import com.logistimo.approval.entity.ApproverQueue;
import java.util.Date;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created by nitisha.khandelwal on 18/05/17.
 */

public class ApprovalSpecifications {


  public static Specification<Approval> withRequesterId(String requesterId) {
    if (requesterId == null) {
      return null;
    }
    return (root, query, cb) -> {
      return cb.equal(root.get("requesterId"), requesterId);
    };
  }

  public static Specification<Approval> withStatus(String status) {
    if (status == null) {
      return null;
    }
    return (root, query, cb) -> {
      return cb.equal(root.get("status"), status);
    };
  }

  public static Specification<Approval> withType(String type) {
    if (type == null) {
      return null;
    }
    return (root, query, cb) -> {
      return cb.equal(root.get("type"), type);
    };
  }

  public static Specification<Approval> withTypeId(String typeId) {
    if (typeId == null) {
      return null;
    }
    return (root, query, cb) -> {
      return cb.equal(root.get("typeId"), typeId);
    };
  }

  public static Specification<Approval> withExpiringInMinutes(String x) {
    if (x == null) {
      return null;
    }
    return (root, query, cb) -> {
      Date currentDatePlusXMinutes = DateUtils.addMinutes(new Date(), Integer.parseInt(x));
      Path<Date> expireAtPath = root.get("expireAt");
      return cb.lessThan(expireAtPath, currentDatePlusXMinutes);
    };
  }

  public static Specification<Approval> withApproverId(String approverId) {
    if (approverId == null) {
      return null;
    }

    return new Specification<Approval>() {
      @Override
      public Predicate toPredicate(Root<Approval> root, CriteriaQuery<?> criteriaQuery,
          CriteriaBuilder criteriaBuilder) {
        Join<Approval, ApproverQueue> approvers = root.join("approverQueue");
        return criteriaBuilder.equal(approvers.get("userId"), approverId);
      }
    };
  }

  public static Specification<Approval> withApproverStatus(String approverId,
      String approverStatus) {
    if (approverId == null || approverStatus == null) {
      return null;
    }

    return new Specification<Approval>() {
      @Override
      public Predicate toPredicate(Root<Approval> root, CriteriaQuery<?> criteriaQuery,
          CriteriaBuilder criteriaBuilder) {
        Join<Approval, ApproverQueue> approvers = root.join("approverQueue");
        return criteriaBuilder.equal(approvers.get("approverStatus"), approverStatus);
      }
    };

  }

  public static Specification<Approval> withApprovalKey(String key, String value) {
    if (key == null || value == null) {
      return null;
    }

    return new Specification<Approval>() {
      @Override
      public Predicate toPredicate(Root<Approval> root, CriteriaQuery<?> criteriaQuery,
          CriteriaBuilder criteriaBuilder) {
        Join<Approval, ApprovalAttributes> attributes = root.join("approvalAttributes");
        return criteriaBuilder.equal(attributes.get("key"), key);
      }
    };
  }

  public static Specification<Approval> withApprovalValue(String key, String value) {
    if (key == null || value == null) {
      return null;
    }

    return new Specification<Approval>() {
      @Override
      public Predicate toPredicate(Root<Approval> root, CriteriaQuery<?> criteriaQuery,
          CriteriaBuilder criteriaBuilder) {
        Join<Approval, ApprovalAttributes> attributes = root.join("approvalAttributes");
        return criteriaBuilder.equal(attributes.get("value"), value);
      }
    };
  }


}
