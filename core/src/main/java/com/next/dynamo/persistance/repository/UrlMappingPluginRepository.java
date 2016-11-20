package com.next.dynamo.persistance.repository;

import com.next.dynamo.persistance.UrlMappingPlugin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlMappingPluginRepository extends JpaRepository<UrlMappingPlugin, Long> {

	List<UrlMappingPlugin> findUrlMappingPluginsByUrlMappingId(Long urlMappingId);

	void deleteUrlMappingPluginByUrlMappingId(Long urlMappingId);
}
