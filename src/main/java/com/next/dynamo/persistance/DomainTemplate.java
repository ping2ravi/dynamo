package com.next.dynamo.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "domain_template")
@Getter 
@Setter
public class DomainTemplate extends BaseEntity {

    @Column(name = "name")
    @NotBlank(message="{domiantemplate.name.empty.error}")
    private String name;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "domain_id")
    @NotNull
    private Domain domain;
    @Column(name = "domain_id", insertable = false, updatable = false)
    private Long domainId;

    @Column(name = "active")
    private boolean active;
    
    @NotBlank(message="{domiantemplate.git.repo.empty.error}")
    @Column(name = "git_repo")
    private String gitRepository;
    
    @NotBlank(message="{domiantemplate.git.branch.empty.error}")
    @Column(name = "git_branch")
    private String gitBranch;

}
