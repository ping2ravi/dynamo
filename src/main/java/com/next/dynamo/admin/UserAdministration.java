package com.next.dynamo.admin;

import org.lightadmin.api.config.AdministrationConfiguration;
import org.lightadmin.api.config.builder.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.api.config.builder.FieldSetConfigurationUnitBuilder;
import org.lightadmin.api.config.builder.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.unit.ScreenContextConfigurationUnit;

import com.next.dynamo.persistance.Domain;

public class UserAdministration extends AdministrationConfiguration<Domain> {
	 
	public EntityMetadataConfigurationUnit configuration( EntityMetadataConfigurationUnitBuilder configurationBuilder ) {
	 return configurationBuilder.nameField( "name" ).build();
	 }
	 
	public ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
	 return screenContextBuilder
	 .screenName( "Users Administration" ).build();
	 }
	 
	public FieldSetConfigurationUnit listView( final FieldSetConfigurationUnitBuilder fragmentBuilder ) {
	 return fragmentBuilder
	 .field( "name" ).caption( "Name" )
	 .field( "setting" ).caption( "Setting" )
	 .build();
	 }

}
