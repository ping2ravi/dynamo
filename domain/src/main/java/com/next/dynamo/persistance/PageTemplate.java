package com.next.dynamo.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorValue(value = "page")
@Getter 
@Setter
@ToString(callSuper=true, exclude={"urlMapping"})
public class PageTemplate extends Template {

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "url_mapping_id", nullable = true)
	//@NotNull(message="{template.urlmapping.null.error}")
    private UrlMapping urlMapping;
    @Column(name = "url_mapping_id", insertable = false, updatable = false)
    private Long urlMappingId;
    
    
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "main_template_id", nullable = true)
	//@NotNull(message="{template.mainTemplate.null.error}")
    private PartTemplate mainTemplate;
    @Column(name = "main_template_id", insertable = false, updatable = false)
    private Long mainTemplateId;

}
