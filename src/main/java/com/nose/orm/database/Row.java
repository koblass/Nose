package com.nose.orm.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

/**
 * Created by Daniel on 10.02.2016.
 */
public class Row extends LinkedHashMap<String, String> {


    /**
     * Create a row from the given result set
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public static Row of(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            Row row = new Row();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                row.put(resultSet.getMetaData().getColumnName(i).toLowerCase(), resultSet.getString(i));
            }
            return row;
        }
        return null;
    }
}
