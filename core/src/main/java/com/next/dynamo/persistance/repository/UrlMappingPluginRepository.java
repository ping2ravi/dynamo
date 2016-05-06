package com.next.dynamo.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.next.dynamo.persistance.UrlMappingPlugin;

public interface UrlMappingPluginRepository extends JpaRepository<UrlMappingPlugin, Long> {

	
}
