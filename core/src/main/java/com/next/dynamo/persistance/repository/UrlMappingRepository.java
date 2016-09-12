package com.next.dynamo.persistance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.next.dynamo.persistance.UrlMapping;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

	List<UrlMapping> findUrlMappingByDomainId(Long domainId);
}
