package com.next.dynamo.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.next.dynamo.persistance.StaticDataPlugin;

public interface StaticDataPluginRepository extends JpaRepository<StaticDataPlugin, Long> {

}
