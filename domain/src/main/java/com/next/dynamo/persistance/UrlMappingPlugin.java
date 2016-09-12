package com.next.dynamo.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "url_mapping_plugin")
@Getter 
@Setter
@ToString(callSuper=true, exclude={"dataPlugin","urlMapping"})
public class UrlMappingPlugin extends BaseEntity{

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name = "url_mapping_id")
    private UrlMapping urlMapping;
    @Column(name = "url_mapping_id", insertable = false, updatable = false)
    private Long urlMappingId;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "data_plugin_id")
    private DataPlugin dataPlugin;
    @Column(name = "data_plugin_id", insertable = false, updatable = false)
    private Long dataPluginId;
	
    @Column(name = "setting", columnDefinition = "LONGTEXT")
    private String setting;
}
