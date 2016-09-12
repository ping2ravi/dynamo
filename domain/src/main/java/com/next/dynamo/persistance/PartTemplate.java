package com.next.dynamo.persistance;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorValue(value = "part")
@Getter 
@Setter
@ToString(callSuper=true)
public class PartTemplate extends Template {

    @Column(name = "part_name")
    private String partName;

}
