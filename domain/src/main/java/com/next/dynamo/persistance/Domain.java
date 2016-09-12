package com.next.dynamo.persistance;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "domain")
@Getter 
@Setter
@ToString(callSuper=true, exclude={"extendedDomain"})
public class Domain extends BaseEntity {

    @Column(name = "name")
    @NotBlank(message="{domian.name.empty.error}")
    private String name;

    @ElementCollection(fetch=FetchType.EAGER)
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
    @JoinColumn(name = "extended_domain_id")
    private Domain extendedDomain;
    @Column(name = "extended_domain_id", insertable = false, updatable = false)
    private Long extendedDomainId;
}
