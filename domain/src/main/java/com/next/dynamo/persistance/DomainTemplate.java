package com.next.dynamo.persistance;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "domain_template")
@Getter 
@Setter
@ToString(callSuper = true, exclude = {"domain", "gitFiles"})
public class DomainTemplate extends BaseEntity {

    @Column(name = "name")
    @NotBlank(message="{domiantemplate.name.empty.error}")
    private String name;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "domain_id")
    @NotNull(message="{domiantemplate.domain.empty.error}")
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

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "domain_template_git_files",
            joinColumns = @JoinColumn(name = "domain_template_id")
    )
    @Column(name = "file_path")
    private Set<String> gitFiles;

}
