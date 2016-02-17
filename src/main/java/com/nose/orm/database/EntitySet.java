package com.nose.orm.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nose.orm.mapping.Entity;
import com.nose.orm.mapping.entity.*;
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
    private Table data;
    private Map<String, Map<String, String>> propertiesSimpleTypeDbData;
    private Map<String, Map<String, List<String>>> propertiesSimpleTypeListDbData;


    public EntitySet(Class<T> entityClass) throws IllegalAccessException, InstantiationException {
        this.entityClass = entityClass;
        this.entityMapping = Entity.factory(entityClass);
        this.data = new Table();
        this.propertiesSimpleTypeDbData = new HashMap<String, Map<String, String>>();
        this.propertiesSimpleTypeListDbData = new HashMap<String, Map<String, List<String>>>();
    }


    /**
     * Populate the entityClass set from the given sql select query.
     *
     * @param dataSource
     * @param select
     */
    public void populateFromSelect(DataSource dataSource, String select) throws SQLException {
        populateFromSelect(dataSource.getConnection(), select);
    }


    /**
     * Populate the entityClass set from the given sql select query.
     *
     * @param connection
     * @param select
     */
    public void populateFromSelect(Connection connection, String select) throws SQLException {
        ResultSet resultSet = connection.createStatement().executeQuery(select);
        int columnCount = resultSet.getMetaData().getColumnCount();
        int rowIndex;
        while (resultSet.next()) {
            this.data.add(new Row());
            rowIndex = this.data.size() - 1;
            for (int i = 1; i <= columnCount; i++) {
                this.data.get(rowIndex).put(resultSet.getMetaData().getColumnName(i).toLowerCase(), resultSet.getString(i));
            }
        }
        resultSet.close();

        populateProperties(connection);
    }


    public List<T> generateTree() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<T> entities = new ArrayList<T>();

        if (!this.data.isEmpty()) {
            for (Row row : this.data) {
                // Populate the own properties
                ObjectNode entity = mapper.createObjectNode();
                for (Map.Entry<String, String> entry : row.entrySet()) {
                    Property property = entityMapping.getPropertyFromColumnName(entry.getKey());
                    if (property != null) {
                        entity.put(property.getName(), entry.getValue());
                    }
                }
                // Populate the 1:1 simple type properties
                for (Map.Entry<String, Map<String, String>> entry : propertiesSimpleTypeDbData.entrySet()) {
                    Property property = entityMapping.getProperty(entry.getKey());
                    String key = property.getConditionUniqueLocalKeyValue(row);
                    if (entry.getValue().containsKey(key)) {
                        entity.put(property.getName(), entry.getValue().get(key));
                    }
                }

                // Populate the 1:n simple type properties
                for (Map.Entry<String, Map<String, List<String>>> entry : propertiesSimpleTypeListDbData.entrySet()) {
                    Property property = entityMapping.getProperty(entry.getKey());
                    String key = property.getConditionUniqueLocalKeyValue(row);
                    if (entry.getValue().containsKey(key)) {
                        ArrayNode valuesNode = entity.putArray(property.getName());
                        for (String value : entry.getValue().get(key)) {
                            valuesNode.add(value);
                        }
                    }
                }

                entities.add(mapper.readValue(entity.toString(), entityClass));
            }
        }
        return entities;
    }


    /**
     * Populates the properties
     */
    protected void populateProperties(Connection connection) throws SQLException {
        if (!data.isEmpty()) {

            // Get the data organised by column instead of row
            Keys keys = data.getKeyValues();

            for (Property property : this.entityMapping.getProperties()) {
                // The entity simple type properties are retrieved
                if (!property.getJoins().isEmpty() && !property.isEntity()) {
                    PreparedStatement preparedStatement = this.createSimpleTypePropertyPopulationQuery(connection, property, keys);
                    if (preparedStatement != null) {
                        ResultSet resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                            Row row = Row.of(resultSet);
                            String key = property.getConditionUniqueForeignKeyValue(row);
                            if (key != null && !key.isEmpty()) {
                                if (property.isList()) {
                                    // The entity simple type 1:n lists properties
                                    if (!propertiesSimpleTypeListDbData.containsKey(property.getName())) {
                                        propertiesSimpleTypeListDbData.put(property.getName(), new HashMap<String, List<String>>());
                                    }
                                    if (!propertiesSimpleTypeListDbData.get(property.getName()).containsKey(key)) {
                                        propertiesSimpleTypeListDbData.get(property.getName()).put(key, new ArrayList<String>());
                                    }
                                    propertiesSimpleTypeListDbData.get(property.getName()).get(key).add(row.get(property.getColumnName()));
                                } else {
                                    // The entity simple type 1:1 properties
                                    if (!propertiesSimpleTypeDbData.containsKey(property.getName())) {
                                        propertiesSimpleTypeDbData.put(property.getName(), new HashMap<String, String>());
                                    }
                                    if (!propertiesSimpleTypeDbData.get(property.getName()).containsKey(key)) {
                                        propertiesSimpleTypeDbData.get(property.getName()).put(key, row.get(property.getColumnName()));
                                    }
                                }
                            }
                        }
                        resultSet.close();
                    }
                }
            }

//
//            // The entity complex type properties are retrieved
//            foreach ($this->mappingObject->getProperties() as $property) {
//                if (($property->getRelationType() == Object\Property::RELATION_TYPE_ONE_TO_ONE ||
//                        $property->getRelationType() == Object\Property::RELATION_TYPE_ONE_TO_MANY) && !$property->getIsSimpleType()) {
//                    $this->propertiesEntity[$property->getName()]->populateFromProperty($property, $keys);
//                }
//            }
        }
    }


    /**
     * Find the right row by the given primary keys.
     *
     * @param prinaryKey
     * @return string[]
     */
    protected Row findRowByPrimaryKeys(Key prinaryKey) {
        List<Row> rows = findRowByKeys(prinaryKey);
        if (rows.size() > 0) {
            return rows.get(0);
        }
        return null;
    }


    /**
     * Find the rows that matches the given keys.
     *
     * @param key The key
     * @return string[][]
     */
    protected List<Row> findRowByKeys(Key key) {
        List<Row> rows = new ArrayList<Row>();
        for (Row row : this.data) {
            boolean found = true;
            for (Map.Entry<String, String> entry : key.entrySet()) {
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
    protected PreparedStatement createSimpleTypePropertyPopulationQuery(Connection connection, Property property, Keys keys) throws SQLException {
        List<String> columns = new ArrayList<String>();
        List<String> whereConditions = new ArrayList<String>();
        StringBuilder select = new StringBuilder();
        List<String> values = new LinkedList<String>();

        for (Join join : property.getJoins()) {
            if (join instanceof JoinColumn && keys.containsKey(((JoinColumn) join).getSourceColumn())) {
                if (!columns.contains(property.getColumnName())) {
                    columns.add(property.getColumnName());
                }
                columns.add(((JoinColumn) join).getTargetColumn());
                values.addAll(keys.get(((JoinColumn) join).getSourceColumn()));
                whereConditions.add(((JoinColumn) join).getTargetColumn() + " in (" + StringUtils.join(Collections.nCopies(keys.get(((JoinColumn) join).getSourceColumn()).size(), "?"), ",") + ")");
            } else if (join instanceof JoinValue) {
                values.add(((JoinValue) join).getValue());
                whereConditions.add(((JoinValue) join).getTargetColumn() + " = ?");
            }
        }
        // Todo: Fix this when implementing the joinTable pattern
        if (columns.isEmpty()) {
            return null;
        }
        select.append("select ");
        select.append(StringUtils.join(columns, ","));
        select.append(" from ");
        select.append(property.getTableName());
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
