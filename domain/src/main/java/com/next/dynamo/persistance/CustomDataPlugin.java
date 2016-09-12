package com.next.dynamo.persistance;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorValue(value = "CustomData")
@Getter 
@Setter
@ToString(callSuper=true)
public class CustomDataPlugin extends DataPlugin {

    @Column(name = "full_class_name")
    @NotBlank(message="{customdataplugin.fullclassname.empty.error}")
    private String fullClassName;

}
