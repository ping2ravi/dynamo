package com.next.dynamo.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.next.dynamo.persistance.DomainTemplate;

public interface DomainTemplateRepository extends JpaRepository<DomainTemplate, Long> {
	
	@Query("select D from DomainTemplate D where D.domainId=?1 and active=true")
	DomainTemplate findActiveDomainTemplateByDomainId(Long domainId);

}
