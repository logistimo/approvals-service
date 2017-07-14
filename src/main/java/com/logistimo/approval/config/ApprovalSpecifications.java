package com.logistimo.approval.config;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalAttributes;
import com.logistimo.approval.entity.ApprovalDomainMapping;
import com.logistimo.approval.entity.ApproverQueue;
import java.util.Date;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created by nitisha.khandelwal on 18/05/17.
 */

public class ApprovalSpecifications {

  public static Specification<Approval> withSoureDomainId(Long domainId) {
    if (domainId == null) {
      return null;
    }

    return (root, query, cb) -> cb.equal(root.get("sourceDomainId"), domainId);
  }

  public static Specification<Approval> withRequesterId(String requesterId) {
    if (requesterId == null) {
      return null;
    }
    return (root, query, cb) -> cb.equal(root.get("requesterId"), requesterId);
  }

  public static Specification<Approval> withStatus(String status) {
    if (status == null) {
      return null;
    }
    return (root, query, cb) -> cb.equal(root.get("status"), status);
  }

  public static Specification<Approval> withType(String type) {
    if (type == null) {
      return null;
    }
    return (root, query, cb) -> cb.equal(root.get("type"), type);
  }

  public static Specification<Approval> withTypeId(String typeId) {
    if (typeId == null) {
      return null;
    }
    return (root, query, cb) -> cb.equal(root.get("typeId"), typeId);
  }

  public static Specification<Approval> withExpiringInMinutes(Integer mins) {
    if (mins == null) {
      return null;
    }
    return (root, query, cb) -> {
      Date currentDatePlusExpiryMinutes = DateUtils.addMinutes(new Date(), mins);
      Path<Date> expireAtPath = root.get("expireAt");
      return cb.lessThan(expireAtPath, currentDatePlusExpiryMinutes);
    };
  }

  public static Specification<Approval> withApproverId(String approverId) {
    if (approverId == null) {
      return null;
    }

    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Approval, ApproverQueue> approvers = root.join("approvers");
      return criteriaBuilder.equal(approvers.get("userId"), approverId);
    };
  }

  public static Specification<Approval> withApproverStatus(String approverStatus) {
    if (approverStatus == null) {
      return null;
    }

    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Approval, ApproverQueue> approvers = root.join("approvers");
      return criteriaBuilder.equal(approvers.get("approverStatus"), approverStatus);
    };

  }

  public static Specification<Approval> withApprovalKey(String key) {
    if (key == null) {
      return null;
    }

    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Approval, ApprovalAttributes> attributes = root.join("attributes");
      return criteriaBuilder.equal(attributes.get("key"), key);
    };
  }

  public static Specification<Approval> withApprovalValue(String value) {
    if (value == null) {
      return null;
    }

    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Approval, ApprovalAttributes> attributes = root.join("attributes");
      return criteriaBuilder.equal(attributes.get("value"), value);
    };
  }

  public static Specification<Approval> withDomainId(Long domainId) {
    if (domainId == null) {
      return null;
    }

    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Approval, ApprovalDomainMapping> domains = root.join("domains");
      return criteriaBuilder.equal(domains.get("domainId"), domainId);
    };
  }
}
