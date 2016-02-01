package com.nose.orm.mapping.entity;

import com.nose.orm.adapter.IAdapter;
import com.nose.orm.mapping.Entity;
import com.google.common.base.CaseFormat;
import com.nose.orm.adapter.Default;
import com.nose.orm.mapping.annotation.Column;
import com.nose.orm.mapping.annotation.Joins;
import com.nose.orm.mapping.annotation.Transcient;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Daniel on 02.11.15.
 */
public class Property {

    protected Entity entity;
    protected Field field;
    protected IAdapter adapter;
    protected String tableName;
    protected String columnName;
    protected boolean isEntity;
    protected boolean isTranscient = false;
    protected boolean isList;
    protected Class type;
    protected List<Join> joins = new ArrayList<Join>();

    public Property(Entity entity, Field field) throws IllegalAccessException, InstantiationException {
        this.entity = entity;
        this.field = field;

        isTranscient = field.isAnnotationPresent(Transcient.class);
        isEntity = field.getType().isAnnotationPresent(com.nose.orm.mapping.annotation.Entity.class);
        isList = field.getType().isArray() || Collection.class.isAssignableFrom(field.getType());
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            this.type = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
        } else {
            this.type = field.getType();
        }
        if (field.isAnnotationPresent(Column.class)) {
            columnName = field.getAnnotation(Column.class).name();
        } else {
            columnName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        }
        if (isEntity()) {
            tableName = Entity.factory(getType()).getTableName();
        } else if (field.isAnnotationPresent(Column.class) && !field.getAnnotation(Column.class).table().isEmpty()) {
            tableName = field.getAnnotation(Column.class).table();
        } else {
            tableName = entity.getTableName();
        }
        if (field.isAnnotationPresent(Column.class)) {
            adapter = field.getAnnotation(Column.class).adapter().newInstance();
        } else {
            adapter = new Default();
        }
        if (field.isAnnotationPresent(Joins.class)) {
            for (com.nose.orm.mapping.annotation.Join joinAnnotation : field.getAnnotation(Joins.class).value()) {
                this.joins.add(Join.create(joinAnnotation));
            }
        }
        if (field.isAnnotationPresent(com.nose.orm.mapping.annotation.Join.class)) {
            this.joins.add(Join.create(field.getAnnotation(com.nose.orm.mapping.annotation.Join.class)));
        }
    }

    /**
     * Return the property name
     */
    public String getName() {
        return field.getName();
    }


    /**
     * Return the column name
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Return the table name
     *
     * @return
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Return true is the property is isTranscient, this mean that the value is not saved.
     */
    public boolean isTranscient() {
        return isTranscient;
    }

    /**
     * True if the property type is a primitive
     */
    public boolean isEntity() {
        return isEntity;
    }

    /**
     * Return the property type
     */
    public Class getType() {
        return type;
    }

    /**
     * Return true if the property contains a list of elements
     */
    public boolean isList() {
        return isList;
    }

    /**
     * Return the adapter used to retrieve and store the data of this property
     * @return
     */
    public IAdapter getAdapter() {
        return adapter;
    }

    /**
     * Return the list of joins
     * @return
     */
    public List<Join> getJoins() {
        return joins;
    }

}
