package com.next.dynamo.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "templates")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "template_type")
@Getter 
@Setter
public class Template extends BaseEntity{

	@Column(name = "html_content", columnDefinition = "LONGTEXT")
    private String htmlContent;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_template_id")
    private DomainTemplate domainTemplate;
    @Column(name = "domain_template_id", insertable = false, updatable = false)
    private Long domainTemplateId;

	@Column(name = "git_file_path")
    private String gitFilePath;
}
