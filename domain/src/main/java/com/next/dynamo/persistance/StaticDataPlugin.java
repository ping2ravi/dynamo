package com.next.dynamo.persistance;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorValue(value = "StaticData")
@Getter 
@Setter
@ToString(callSuper=true)
public class StaticDataPlugin extends DataPlugin {

    @Column(name = "content", columnDefinition = "LONGTEXT")
    @NotBlank(message="{staticdataplugin.content.empty.error}")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
