package com.next.dynamo.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.next.dynamo.persistance.DataPlugin;

public interface DataPluginRepository extends JpaRepository<DataPlugin, Long> {

	
}
