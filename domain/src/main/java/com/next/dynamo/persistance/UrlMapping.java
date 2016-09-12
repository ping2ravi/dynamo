package com.next.dynamo.persistance;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "url_mapping")
@Getter 
@Setter
@ToString(callSuper=true, exclude={"domain"})
public class UrlMapping extends BaseEntity {

    @Column(name = "url_pattern")
    @NotBlank(message="{urlmapping.urlpattern.empty.error}")
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

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "domain_id")
    @NotNull(message="{urlmapping.domain.null.error}")
    private Domain domain;
    @Column(name = "domain_id", insertable = false, updatable = false)
    private Long domainId;

}
