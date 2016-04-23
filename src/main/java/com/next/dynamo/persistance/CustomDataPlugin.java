package com.next.dynamo.persistance;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "CustomData")
@Getter 
@Setter
public class CustomDataPlugin extends DataPlugin {

    @Column(name = "full_class_name")
    private String fullClassName;

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    @Override
    public String toString() {
        return "CustomDataPlugin [fullClassName=" + fullClassName + ", toString()=" + super.toString() + "]";
    }

}
