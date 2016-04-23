package com.next.dynamo.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "domain_template")
@Getter 
@Setter
public class DomainTemplate extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "domain_id")
    private Domain domain;
    @Column(name = "domain_id", insertable = false, updatable = false)
    private Long domainId;

    @Column(name = "active")
    private boolean active;
    
    @Column(name = "git_Repo")
    private String gitRepository;
    
    @Column(name = "git_branch")
    private String gitBranch;

}
