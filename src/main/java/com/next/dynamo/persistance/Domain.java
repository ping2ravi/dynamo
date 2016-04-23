package com.next.dynamo.persistance;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "domain")
@Getter 
@Setter
public class Domain extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ElementCollection
    @CollectionTable(
          name="domain_alias",
          joinColumns=@JoinColumn(name="domain_id")
    )
    @Column(name = "alias")
    private Set<String> aliases;


    @Column(name = "active")
    private boolean active;
    
    @Column(name = "setting", columnDefinition = "LONGTEXT")
    private String setting;
    
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "extended_omain_id")
    private Domain extendedDomain;
    @Column(name = "extended_domain_id", insertable = false, updatable = false)
    private Long extendedDomainId;
}
