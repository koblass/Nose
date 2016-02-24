package com.nose.orm.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;

/**
 * Created by Daniel on 10.02.2016.
 */
public class Row extends LinkedHashMap<String, String> {


    protected static DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    protected static DateFormat DEFAULT_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    protected static DateFormat DEFAULT_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000+0000");


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
            int columnCount = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                int columnType = resultSet.getMetaData().getColumnType(i);
                String value;
                switch (columnType) {
                    case Types.DATE :
                        value = DEFAULT_DATE_FORMAT.format(resultSet.getDate(i));
                        break;
                    case Types.TIME :
                        value = DEFAULT_TIME_FORMAT.format(resultSet.getTime(i));
                        break;
                    case Types.TIMESTAMP :
                        value = DEFAULT_DATETIME_FORMAT.format(resultSet.getTimestamp(i));
                        break;
                    default :
                        value = resultSet.getString(i);
                }
                row.put(resultSet.getMetaData().getColumnName(i).toLowerCase(), value);
            }
            return row;
        }
        return null;
    }
}
