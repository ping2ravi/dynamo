package com.next.dynamo.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.next.dynamo.persistance.Domain;

public interface DomainRepository extends JpaRepository<Domain, Long> {

}
