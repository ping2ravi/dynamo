package com.next.dynamo.persistance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.next.dynamo.persistance.PartTemplate;

public interface PartTemplateRepository extends JpaRepository<PartTemplate, Long> {

	List<PartTemplate> findPartTemplatesByDomainTemplateId(Long domainTemplateId);
}
