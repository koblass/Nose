package com.nose.orm.mapping.entity;

import com.google.common.base.CaseFormat;
import com.nose.orm.adapter.Default;
import com.nose.orm.adapter.IAdapter;
import com.nose.orm.database.Row;
import com.nose.orm.mapping.Entity;
import com.nose.orm.mapping.annotation.Column;
import com.nose.orm.mapping.annotation.Joins;
import com.nose.orm.mapping.annotation.Orders;
import com.nose.orm.mapping.annotation.Transcient;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
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
    protected JoinTable joinTable = null;
    protected List<Join> joins = new ArrayList<Join>();
    protected List<Order> orders = new ArrayList<Order>();

    public Property(Entity entity, Field field) throws IllegalAccessException, InstantiationException {
        this.entity = entity;
        this.field = field;

        isTranscient = field.isAnnotationPresent(Transcient.class);
        isList = field.getType().isArray() || Collection.class.isAssignableFrom(field.getType());
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            this.type = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
        } else if (type instanceof Class && ((Class) type).isArray()) {
            this.type = ((Class) type).getComponentType();
        } else {
            this.type = field.getType();
        }
        isEntity = this.type.isAnnotationPresent(com.nose.orm.mapping.annotation.Entity.class);
        if (field.isAnnotationPresent(Column.class) && StringUtils.isNotEmpty(field.getAnnotation(Column.class).name())) {
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
            this.joins.addAll(Join.create(field.getAnnotation(Joins.class).value()));
        }
        if (field.isAnnotationPresent(com.nose.orm.mapping.annotation.Join.class)) {
            this.joins.add(Join.create(field.getAnnotation(com.nose.orm.mapping.annotation.Join.class)));
        }
        if (field.isAnnotationPresent(com.nose.orm.mapping.annotation.JoinTable.class)) {
            this.joinTable = JoinTable.create(field.getAnnotation(com.nose.orm.mapping.annotation.JoinTable.class));
        }
        if (field.isAnnotationPresent(Orders.class)) {
            this.orders.addAll(Order.create(field.getAnnotation(Orders.class).value()));
        }
        if (field.isAnnotationPresent(com.nose.orm.mapping.annotation.Order.class)) {
            this.orders.add(Order.create(field.getAnnotation(com.nose.orm.mapping.annotation.Order.class)));
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
     *
     * @return
     */
    public IAdapter getAdapter() {
        return adapter;
    }

    /**
     * Return the list of joins
     *
     * @return
     */
    public List<Join> getJoins() {
        return joins;
    }

    /**
     * Return the join table
     * @return
     */
    public JoinTable getJoinTable() {
        return joinTable;
    }

    /**
     * Return the list of invoices
     *
     * @return
     */
    public List<Order> getOrders() {
        return orders;
    }


    /**
     * Creates a unique key used to store the raw data in a tree structure in order to speed up the data browsing
     */
    public String getConditionUniqueLocalKeyValue(Row row) {
        List<String> keys = new LinkedList<String>();
        for (Join join : joins) {
            if (join instanceof JoinColumn && row.containsKey(((JoinColumn) join).getSourceColumn())) {
                keys.add(row.get(((JoinColumn) join).getSourceColumn()));
            } else if (join instanceof JoinValue) {
                keys.add(((JoinValue) join).getValue());
            }
        }
        return keys.toString();
    }


    /**
     * Creates a unique key used to store the raw data in a tree structure in order to speed up the data browsing
     */
    public String getConditionUniqueForeignKeyValue(Row row) {
        List<String> keys = new LinkedList<String>();
        for (Join join : joins) {
            if (join instanceof JoinColumn && row.containsKey(((JoinColumn) join).getTargetColumn())) {
                keys.add(row.get(((JoinColumn) join).getTargetColumn()));
            } else if (join instanceof JoinValue) {
                keys.add(((JoinValue) join).getValue());
            }
        }
        return keys.toString();
    }
}
