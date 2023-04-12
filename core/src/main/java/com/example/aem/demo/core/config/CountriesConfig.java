package com.example.aem.demo.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name="my demo - Countries JSON configuration",
        description = "Countries JSON Factory configuration.")
public @interface CountriesConfig {
    @AttributeDefinition(
            name = "Country Code",
            description = "Add Country code.",
            type = AttributeType.STRING
    )
    public String countryCode() default "us";

    @AttributeDefinition(
            name = "JSON Name",
            description = "Name for JSON File.",
            type = AttributeType.STRING
    )
    public String jsonName() default "us-authors.json";

    @AttributeDefinition(
            name = "JSON Path",
            description = "Path for JSON File.",
            type = AttributeType.STRING
    )
    public String jsonPath() default "/content/mydemo/us";

    @AttributeDefinition(
            name = "Users Node Name",
            description = "Parent node name for userss.",
            type = AttributeType.STRING
    )
    public String usersNode() default "us-users";

    @AttributeDefinition(
            name = "Users Node Path",
            description = "Path for Parent author node.",
            type = AttributeType.STRING
    )
    public String usersPath() default "/content/mydemo/us";
}