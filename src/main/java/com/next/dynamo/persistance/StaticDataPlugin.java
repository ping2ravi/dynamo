package com.next.dynamo.persistance;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "StaticData")
@Getter 
@Setter
public class StaticDataPlugin extends DataPlugin {

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "StaticDataPlugin [content=" + content + ", toString()=" + super.toString() + "]";
    }

}
