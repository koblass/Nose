package com.nose.orm.mapping;

import com.google.common.base.CaseFormat;
import com.nose.orm.adapter.IAdapter;
import com.nose.orm.mapping.annotation.Id;
import com.nose.orm.mapping.entity.PrimaryKey;
import com.nose.orm.mapping.entity.Property;

import java.lang.reflect.Method;
import java.util.*;

/**
 * This class represent the ORM mapping object
 *
 * @author Kobler Daniel <daniel.kobler@gmail.com>
 */
public class Entity {

    /**
     * The represented class
     */
    protected Class cls = null;


    /**
     * The database table name
     */
    protected String tableName = null;


    /**
     * The list of primary properties
     */
    protected PrimaryKey primaryKey = null;


    /**
     * The orm adapter name
     */
    protected IAdapter adapter;


    /**
     * The properties
     */
    protected Map<String, Property> properties = new HashMap<String, Property>();



    /**
     * The list of mapping object instances
     */
    protected static Map<Class, Entity> instances = new HashMap<Class, Entity>();



    /**
     * Returns the mapping objects associated to the given class name
     */
    public static Entity factory(Class cls) throws InstantiationException, IllegalAccessException {
        if ( !instances.containsKey(cls)) {
            instances.put(cls, new Entity(cls));
        }
        return instances.get(cls);
    }



    /**
     * The constructor
     */
    protected Entity(Class cls) throws IllegalAccessException, InstantiationException {
        this.cls = cls;

        com.nose.orm.mapping.annotation.Entity entityAnnotation = (com.nose.orm.mapping.annotation.Entity) cls.getAnnotation(com.nose.orm.mapping.annotation.Entity.class);
        this.tableName = entityAnnotation.table();
        if (this.tableName.isEmpty()) {
            this.tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, cls.getSimpleName());
        }
        this.adapter = entityAnnotation.adapter().newInstance();

        ArrayList<Property> primaryProperties = new ArrayList<Property>();
        for (final java.lang.reflect.Field field : cls.getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null) {
                primaryProperties.add(new Property(this, field));
            }
            this.properties.put(field.getName(), new Property(this, field));
        }
        this.primaryKey = new PrimaryKey(primaryProperties);
    }


    /**
     * Return the class
     */
    public Class getCls() {
        return cls;
    }


    /**
     * Return the table name
     */
    public String getTableName() {
        return tableName;
    }


    /**
     * Return the orm adapter used by this object
     */
    public IAdapter getAdapter() {
        return adapter;
    }



    /**
     * Return the public method in this object
     */
    public Collection<Method> getPublicMethods() {
        return Arrays.asList(cls.getMethods());
    }



    /**
     * Return true if the class contain the requested method declared as public
     */
    public boolean hasPublicMethod(String methodName) {
        return getPublicMethods().contains(methodName);
    }



    /**
     * Return the properties present in this object
     */
    public Collection<Property> getProperties() {
        return properties.values();
    }



    /**
     * Return true if the requested property is present in this object
     */
    public boolean hasProperty(String propertyName) {
        return properties.containsKey(propertyName);
    }



    /**
     * Return the requested property
     */
    public Property getProperty(String propertyName) {
        return hasProperty(propertyName) ? properties.get(propertyName) : null;
    }


    /**
     * Return the property object that matches with the given column name
     */
    public Property getPropertyFromColumnName(String columnName) {
        for (final Property property : getProperties()) {
            if (property.getColumnName().equals(columnName)) {
                return property;
            }
        }
        return null;
    }


    /**
     * Return the primary keys
     */
    public PrimaryKey getPrimaryKeys() {
        return primaryKey;
    }

}