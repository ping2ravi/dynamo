package com.next.dynamo.persistance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.next.dynamo.persistance.PageTemplate;

public interface PageTemplateRepository extends JpaRepository<PageTemplate, Long> {

	List<PageTemplate> findPageTemplatesByDomainTemplateId(Long domainTemplateId);
}
