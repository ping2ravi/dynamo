package com.next.dynamo.persistance;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "page")
@Getter 
@Setter
@ToString(callSuper = true, exclude = {"urlMapping", "mainTemplate"})
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
