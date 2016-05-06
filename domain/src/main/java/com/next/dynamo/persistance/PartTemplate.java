package com.next.dynamo.persistance;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "part")
@Getter 
@Setter
public class PartTemplate extends Template {

    @Column(name = "part_name")
    private String partName;

}
