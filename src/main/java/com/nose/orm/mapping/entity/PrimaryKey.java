package com.nose.orm.mapping.entity;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Daniel on 28.01.2016.
 */
public class PrimaryKey {

    private Collection<Property> properties;


    public PrimaryKey(Property... properties) {
        this.properties = Arrays.asList(properties);
    }


    public PrimaryKey(Collection<Property> properties) {
        this.properties = properties;
    }


    public Collection<Property> getProperties() {
        return properties;
    }


    public Property getProperty(String propertyName) {
        for (Property property : properties) {
            if (property.getName().equals(propertyName)) {
                return property;
            }
        }
        return null;
    }
}
