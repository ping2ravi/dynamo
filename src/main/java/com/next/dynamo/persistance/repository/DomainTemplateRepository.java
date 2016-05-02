package com.next.dynamo.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.next.dynamo.persistance.DomainTemplate;

public interface DomainTemplateRepository extends JpaRepository<DomainTemplate, Long> {

}
