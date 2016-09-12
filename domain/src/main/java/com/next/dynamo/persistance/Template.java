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
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "templates")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "template_type")
@Getter 
@Setter
@ToString(callSuper=true, exclude={"domainTemplate"})
public class Template extends BaseEntity{

	@Column(name = "html_content", columnDefinition = "LONGTEXT")
    private String htmlContent;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_template_id")
    @NotNull(message="{template.domaintemplate.null.error}")
    private DomainTemplate domainTemplate;
    @Column(name = "domain_template_id", insertable = false, updatable = false)
    private Long domainTemplateId;

	@Column(name = "git_file_path")
	@NotBlank(message="{template.git.filepath.blank.error}")
    private String gitFilePath;
}
