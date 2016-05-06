package com.next.dynamo.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.next.dynamo.persistance.CustomDataPlugin;

public interface CustomDataPluginRepository extends JpaRepository<CustomDataPlugin, Long> {

	CustomDataPlugin findByFullClassName(String fullClassName);
}
