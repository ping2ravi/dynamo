package com.next.dynamo.persistance.repository;

import com.next.dynamo.persistance.PartTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartTemplateRepository extends JpaRepository<PartTemplate, Long> {

	List<PartTemplate> findPartTemplatesByDomainTemplateId(Long domainTemplateId);

	@Query("select partTemplate from PartTemplate partTemplate where domainTemplateId=? and partType='Main'")
	List<PartTemplate> findMainPartTemplatesByDomainTemplateId(Long domainTemplateId);
}
