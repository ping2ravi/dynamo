package com.next.dynamo.persistance;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "domain")
@Getter 
@Setter
@ToString(callSuper=true, exclude={"extendedDomain"})
public class Domain extends BaseEntity {

    @Column(name = "name")
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
