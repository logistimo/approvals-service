package com.logistimo.approval.config;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalAttributes;
import com.logistimo.approval.entity.ApprovalDomainMapping;
import com.logistimo.approval.entity.ApproverQueue;
import com.logistimo.approval.models.AttributeFilter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

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
    if (StringUtils.isBlank(type)) {
      return null;
    }
    return (root, query, cb) -> cb.equal(root.get("type"), type);
  }

  public static Specification<Approval> withTypeId(String typeId) {
    if (StringUtils.isBlank(typeId)) {
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

  public static Specification<Approval> withAttributes(List<AttributeFilter> attributes) {
    if (attributes == null || attributes.isEmpty()) {
      return null;
    }
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>(1);
      for (AttributeFilter attribute : attributes) {
        if (attribute.getKey() == null || attribute.getValues() == null || attribute.getValues()
            .isEmpty()) {
          continue;
        }

        Join<Approval, ApprovalAttributes> attributesColumn = root.join("attributes");
        predicates.add(criteriaBuilder
            .and(criteriaBuilder.equal(attributesColumn.get("key"), attribute.getKey()),
                attributesColumn.get("value").in(attribute.getValues())));
      }

      return predicates.isEmpty() ? null : criteriaBuilder
          .and(predicates.toArray(new Predicate[predicates.size()]));
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
