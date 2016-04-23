package com.next.dynamo.persistance;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "data_plugin")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "plugin_type")
@Getter 
@Setter
public class DataPlugin extends BaseEntity{

	
    @Column(name = "disabled")
    private boolean disabled;
    @Column(name = "plugin_name")
    private String pluginName;
    @Column(name = "global", columnDefinition = "BIT(1) DEFAULT 0")
    private boolean global;

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    @Override
    public String toString() {
        return "DataPlugin [id=" + id + ", ver=" + ver + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + ", creatorId=" + creatorId + ", modifierId=" + modifierId + ", disabled="
                + disabled + ", pluginName=" + pluginName + "]";
    }
	


	
}
