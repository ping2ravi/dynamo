package com.next.dynamo.persistance;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DiscriminatorOptions;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "templates")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "template_type")
@DiscriminatorOptions(force = true)
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
