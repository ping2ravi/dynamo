package com.next.dynamo.persistance;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "CustomData")
@Getter 
@Setter
public class CustomDataPlugin extends DataPlugin {

    @Column(name = "full_class_name")
    @NotBlank(message="{customdataplugin.fullclassname.empty.error}")
    private String fullClassName;

    @Override
    public String toString() {
        return "CustomDataPlugin [fullClassName=" + fullClassName + ", toString()=" + super.toString() + "]";
    }

}
