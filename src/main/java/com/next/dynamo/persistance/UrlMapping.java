package com.next.dynamo.persistance;

import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "url_mapping")
@Getter 
@Setter
public class UrlMapping extends BaseEntity {

    @Column(name = "url_pattern")
    private String urlPattern;

   
    @ElementCollection
    @CollectionTable(
          name="url_alias",
          joinColumns=@JoinColumn(name="url_mapping_id")
    )
    @Column(name = "alias")
    private Set<String> aliases;

    @Column(name = "active")
    private boolean active;

    @Column(name = "secured")
    private boolean secured;
    
    @Column(name = "forward_url")
    private String forwardUrl;

    @Column(name = "http_cache_time_seconds")
    private Integer httpCacheTimeSeconds;

    @OneToMany(mappedBy = "urlMapping", fetch = FetchType.LAZY)
    private List<UrlMappingPlugin> urlMappingPlugins;

}
