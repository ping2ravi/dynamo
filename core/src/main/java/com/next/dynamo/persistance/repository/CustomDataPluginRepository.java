package com.next.dynamo.persistance.repository;

import com.next.dynamo.persistance.CustomDataPlugin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomDataPluginRepository extends JpaRepository<CustomDataPlugin, Long> {

	CustomDataPlugin findByFullClassName(String fullClassName);

    @Query("select cdp from CustomDataPlugin cdp where disabled=false order by pluginName asc")
    List<CustomDataPlugin> getActiveCustomDataPlugins();
}
