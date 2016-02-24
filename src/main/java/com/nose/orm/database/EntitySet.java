package com.nose.orm.database;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nose.orm.mapping.Entity;
import com.nose.orm.mapping.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * The entityClass set contains all the data necessary to build the tree of entities
 * Created by Daniel on 02.02.2016.
 */
public class EntitySet<T> {


    private Class<T> entityClass;
    private Entity entityMapping;
    private Table dbData;
    private Map<String, Map<String, String>> propertiesSimpleTypeDbData;
    private Map<String, Map<String, List<String>>> propertiesSimpleTypeListDbData;
    private Map<String, EntitySet> propertiesEntity;


    private EntitySet(Class<T> entityClass) throws IllegalAccessException, InstantiationException {
        this.entityClass = entityClass;
        this.entityMapping = Entity.factory(entityClass);
        this.dbData = new Table();
        this.propertiesSimpleTypeDbData = new HashMap<String, Map<String, String>>();
        this.propertiesSimpleTypeListDbData = new HashMap<String, Map<String, List<String>>>();
        propertiesEntity = new HashMap<String, EntitySet>();
    }

    public static EntitySet create(Class entityClass) throws InstantiationException, IllegalAccessException {
        return new EntitySet(entityClass);
    }

    /**
     * Creates the query used to populate the entity and all its simple type 1:1 relations
     *
     * @param entity
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static String createEntityPopulationQuery(Entity entity, Set<String> additionalColumns) throws IllegalAccessException, InstantiationException {
        Set<String> columns = new LinkedHashSet<String>();
        if (CollectionUtils.isNotEmpty(additionalColumns)) {
            columns.addAll(additionalColumns);
        }

        for (Property property : entity.getProperties()) {
            if (property.getJoins().isEmpty()) {
                // This is not a remote property
                columns.add(property.getColumnName());
            } else {
                // This is a remote property
                for (Join join : property.getJoins()) {
                    if (join instanceof JoinColumn) {
                        columns.add(((JoinColumn) join).getSourceColumn());
                    }
                }
            }
        }
        if (columns.isEmpty()) {
            return null;
        }
        return new StringBuilder()
                .append("select ")
                .append(StringUtils.join(columns, ","))
                .append(" from ")
                .append(entity.getTableName())
                .toString();
    }

    public List<T> generateTree() throws IOException, IllegalAccessException, NoSuchFieldException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        List<T> entities = new ArrayList<T>();

        // Generate the json tree
        ArrayNode jsonObjectsList = generateJsonTree(new Keys());

        // Convert to POJO
        for (JsonNode jsonNode : jsonObjectsList) {
            // The entity is created and added to the list
            T entity = mapper.readValue(jsonNode.toString(), entityClass);
            entities.add(entity);
        }

        return entities;
    }


    protected ArrayNode generateJsonTree(Keys keys) throws IOException, IllegalAccessException, NoSuchFieldException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode entities = mapper.createArrayNode();

        if (!this.dbData.isEmpty()) {
            for (Row row : this.dbData) {
                if (keys.matches(row)) {

                    // Populate the own properties
                    ObjectNode objectNode = mapper.createObjectNode();
                    for (Map.Entry<String, String> entry : row.entrySet()) {
                        Property property = entityMapping.getPropertyFromColumnName(entry.getKey());
                        if (property != null) {
                            objectNode.put(property.getName(), entry.getValue());
                        }
                    }
                    // Populate the 1:1 simple type properties
                    for (Map.Entry<String, Map<String, String>> entry : propertiesSimpleTypeDbData.entrySet()) {
                        Property property = entityMapping.getProperty(entry.getKey());
                        String propertyKeys = property.getConditionUniqueLocalKeyValue(row);
                        if (entry.getValue().containsKey(propertyKeys)) {
                            objectNode.put(property.getName(), entry.getValue().get(propertyKeys));
                        }
                    }

                    // Populate the 1:n simple type properties
                    for (Map.Entry<String, Map<String, List<String>>> entry : propertiesSimpleTypeListDbData.entrySet()) {
                        Property property = entityMapping.getProperty(entry.getKey());
                        String propertyKeys = property.getConditionUniqueLocalKeyValue(row);
                        if (entry.getValue().containsKey(propertyKeys)) {
                            ArrayNode valuesNode = objectNode.putArray(property.getName());
                            for (String value : entry.getValue().get(propertyKeys)) {
                                valuesNode.add(value);
                            }
                        }
                    }

                    // Populate the 1:1 and 1:n complex type properties
                    for (Map.Entry<String, EntitySet> entry : propertiesEntity.entrySet()) {
                        Property property = entityMapping.getProperty(entry.getKey());
                        Keys propertyKeys = new Keys();
                        for (Join join : property.getJoins()) {
                            if (join instanceof JoinColumn) {
                                propertyKeys.put(((JoinColumn) join).getTargetColumn(), row.get(((JoinColumn) join).getSourceColumn()));
                            }
                        }

                        // Set the field value
                        ArrayNode propertyValues = entry.getValue().generateJsonTree(propertyKeys);
                        if (property.isList()) {
                            objectNode.replace(property.getName(), propertyValues);
                        } else if (propertyValues.size() != 0) {
                            objectNode.replace(property.getName(), propertyValues.get(0));
                        }
                    }

                    // The entity is created and added to the list
                    entities.add(objectNode);
                }
            }
        }
        return entities;
    }

    /**
     * Populate the entityClass set from the given sql select query.
     *
     * @param dataSource
     * @param select
     */
    public void populateFromSelect(DataSource dataSource, String select) throws SQLException, InstantiationException, IllegalAccessException {
        populateFromSelect(dataSource.getConnection(), select);
    }

    /**
     * Populate the entityClass set from the given sql select query.
     *
     * @param connection
     * @param select
     */
    public void populateFromSelect(Connection connection, String select) throws SQLException, IllegalAccessException, InstantiationException {
        ResultSet resultSet = connection.createStatement().executeQuery(select);
        populateEntity(connection, resultSet);
    }

    protected void populateEntity(Connection connection, ResultSet resultSet) throws SQLException, InstantiationException, IllegalAccessException {
        while (resultSet.next()) {
            this.dbData.add(Row.of(resultSet));
        }
        resultSet.close();

        populateProperties(connection);
    }

    /**
     * Populate the entity for the given property
     * Can not be used for simple type properties
     *
     * @param connection
     * @param property
     * @param columnValues
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    protected void populateFromProperty(Connection connection, Property property, ColumnValues columnValues) throws SQLException, IllegalAccessException, InstantiationException {
        PreparedStatement preparedStatement = createComplexTypePropertyPopulationQuery(connection, property, columnValues);
        if (preparedStatement != null) {
            ResultSet resultSet = preparedStatement.executeQuery();
            populateEntity(connection, resultSet);
        }
    }

    /**
     * Populates the properties
     *
     * @param connection
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected void populateProperties(Connection connection) throws SQLException, InstantiationException, IllegalAccessException {
        if (CollectionUtils.isNotEmpty(dbData)) {

            // Get the data organised by column instead of row
            ColumnValues columnValues = dbData.getColumnValues();

            for (Property property : this.entityMapping.getProperties()) {
                // The entity simple type properties are retrieved
                if (!property.getJoins().isEmpty() && !property.isEntity()) {
                    PreparedStatement preparedStatement = this.createSimpleTypePropertyPopulationQuery(connection, property, columnValues);
                    if (preparedStatement != null) {
                        ResultSet resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                            Row row = Row.of(resultSet);
                            String propertyKeys = property.getConditionUniqueForeignKeyValue(row);
                            if (StringUtils.isNotEmpty(propertyKeys)) {
                                if (property.isList()) {
                                    // The entity simple type 1:n lists properties
                                    if (!propertiesSimpleTypeListDbData.containsKey(property.getName())) {
                                        propertiesSimpleTypeListDbData.put(property.getName(), new HashMap<String, List<String>>());
                                    }
                                    if (!propertiesSimpleTypeListDbData.get(property.getName()).containsKey(propertyKeys)) {
                                        propertiesSimpleTypeListDbData.get(property.getName()).put(propertyKeys, new ArrayList<String>());
                                    }
                                    propertiesSimpleTypeListDbData.get(property.getName()).get(propertyKeys).add(row.get(property.getColumnName()));
                                } else {
                                    // The entity simple type 1:1 properties
                                    if (!propertiesSimpleTypeDbData.containsKey(property.getName())) {
                                        propertiesSimpleTypeDbData.put(property.getName(), new HashMap<String, String>());
                                    }
                                    if (!propertiesSimpleTypeDbData.get(property.getName()).containsKey(propertyKeys)) {
                                        propertiesSimpleTypeDbData.get(property.getName()).put(propertyKeys, row.get(property.getColumnName()));
                                    }
                                }
                            }
                        }
                        resultSet.close();
                    }
                } else if (!property.getJoins().isEmpty() && property.isEntity()) {
                    // The entity complex type are retrieved
                    if (!propertiesEntity.containsKey(property.getName())) {
                        propertiesEntity.put(property.getName(), EntitySet.create(property.getType()));
                    }
                    propertiesEntity.get(property.getName()).populateFromProperty(connection, property, columnValues);
                }
            }
        }
    }

    /**
     * Find the right row by the given primary keys.
     *
     * @param prinaryKeys
     * @return string[]
     */
    protected Row findRowByPrimaryKeys(Keys prinaryKeys) {
        List<Row> rows = findRowByKeys(prinaryKeys);
        if (rows.size() > 0) {
            return rows.get(0);
        }
        return null;
    }

    /**
     * Find the rows that matches the given keys.
     *
     * @param keys The keys
     * @return string[][]
     */
    protected List<Row> findRowByKeys(Keys keys) {
        List<Row> rows = new ArrayList<Row>();
        for (Row row : this.dbData) {
            boolean found = true;
            for (Map.Entry<String, String> entry : keys.entrySet()) {
                if (!row.get(entry.getKey()).equals(entry.getValue())) {
                    found = false;
                }
            }
            if (found) {
                rows.add(row);
            }
        }
        return rows;
    }

    /**
     * Creates the query used to populate the 1:1 and 1:n simple type property
     */
    protected PreparedStatement createSimpleTypePropertyPopulationQuery(Connection connection, Property property, ColumnValues columnValues) throws SQLException {
        Set<String> columns = new LinkedHashSet<String>();
        StringBuilder select = new StringBuilder();

        columns.add(property.getColumnName());
        for (Join join : property.getJoins()) {
            if (join instanceof JoinColumn && columnValues.containsKey(((JoinColumn) join).getSourceColumn())) {
                columns.add(((JoinColumn) join).getTargetColumn());
            }
        }
        select.append("select ");
        select.append(StringUtils.join(columns, ","));
        select.append(" from ");
        select.append(property.getTableName());

        return createPropertyPopulationPreparedStatement(select, connection, property, columnValues);
    }

    /**
     * Creates the query used to populate the 1:1 and 1:N complex type property
     *
     * @param connection
     * @param property
     * @param columnValues
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SQLException
     */
    protected PreparedStatement createComplexTypePropertyPopulationQuery(Connection connection, Property property, ColumnValues columnValues) throws IllegalAccessException, InstantiationException, SQLException {
        Entity entity = Entity.factory(property.getType());
        StringBuilder select = new StringBuilder();
        Set<String> columns = new LinkedHashSet<String>();

        // When fetching the entity all the columns used within the joins have to be added on top of the one used by the fields.
        // This is mandatory in order to be able to perform the mapping to recompose the object's mapping.
        for (Join join : property.getJoins()) {
            if (((JoinColumn) join).getTargetTable().equals(entity.getTableName())) {
                columns.add(((JoinColumn) join).getTargetColumn());
            }
        }

        select.append(createEntityPopulationQuery(entity, columns));
        return createPropertyPopulationPreparedStatement(select, connection, property, columnValues);
    }

    /**
     * Creates the query used to populate the 1:1 and 1:n simple type property
     */
    protected PreparedStatement createPropertyPopulationPreparedStatement(StringBuilder select, Connection connection, Property property, ColumnValues columnValues) throws SQLException {
        List<String> whereConditions = new LinkedList<String>();
        List<String> values = new LinkedList<String>();

        for (Join join : property.getJoins()) {
            if (join instanceof JoinColumn && columnValues.containsKey(((JoinColumn) join).getSourceColumn())) {
                values.addAll(columnValues.get(((JoinColumn) join).getSourceColumn()));
                whereConditions.add(((JoinColumn) join).getTargetColumn() + " in (" + StringUtils.join(Collections.nCopies(columnValues.get(((JoinColumn) join).getSourceColumn()).size(), "?"), ",") + ")");
            } else if (join instanceof JoinValue) {
                values.add(((JoinValue) join).getValue());
                whereConditions.add(((JoinValue) join).getTargetColumn() + " = ?");
            }
        }
        if (whereConditions.size() > 0) {
            select.append(" where ");
            for (int i = 0; i < whereConditions.size(); i++) {
                select.append(whereConditions.get(i));
                if (i < whereConditions.size() - 1) {
                    select.append(" and ");
                }
            }
        }
        if (!property.getOrders().isEmpty()) {
            select.append(" order by ");
            for (Order order : property.getOrders()) {
                select.append(order.getColumnName());
                select.append(" ");
                select.append(order.getDirection());
            }
        }

        PreparedStatement preparedStatement = connection.prepareStatement(select.toString());
        for (int i = 0; i < values.size(); i++) {
            if (StringUtils.isNumeric(values.get(i))) {
                preparedStatement.setInt(i + 1, Integer.valueOf(values.get(i)));
            } else {
                preparedStatement.setString(i + 1, values.get(i));
            }
        }

        return preparedStatement;
    }


}
