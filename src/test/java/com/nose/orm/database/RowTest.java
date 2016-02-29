package com.nose.orm.database;

import com.sun.rowset.JdbcRowSetImpl;
import org.junit.Test;

import javax.sql.rowset.RowSetMetaDataImpl;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Daniel on 14.02.2016.
 */
public class RowTest {
    private final static String ID_KEY = "id";
    private final static String ID_VALUE = "1";
    private final static String USERNAME_KEY = "username";
    private final static String USERNAME_VALUE = "jdoe";
    private final static String LAST_NAME_KEY = "last_name";
    private final static String LAST_NAME_VALUE = "joe";
    private final static String FIRST_NAME_KEY = "first_name";
    private final static String FIRST_NAME_VALUE = "john";
    private final static String DATE_KEY = "date";
    private final static String DATE_VALUE = "1970.01.01";
    private final static String DATE_VALUE_FORMATTED = "1970-01-01";
    private final static String TIME_KEY = "time";
    private final static String TIME_VALUE = "12.00.00";
    private final static String TIME_VALUE_FORMATTED = "12:00:00";
    private final static String TIMESTAMP_KEY = "timestamp";
    private final static String TIMESTAMP_VALUE = "1970.01.01 12.00.00";
    private final static String TIMESTAMP_VALUE_FORMATTED = "1970-01-01T12:00:00.000+0000";

    Map<String, String> data = new LinkedHashMap<String, String>();

    public RowTest() {
        data.put(ID_KEY, ID_VALUE);
        data.put(USERNAME_KEY, USERNAME_VALUE);
        data.put(LAST_NAME_KEY, LAST_NAME_VALUE);
        data.put(FIRST_NAME_KEY, FIRST_NAME_VALUE);
        data.put(DATE_KEY, DATE_VALUE);
        data.put(TIME_KEY, TIME_VALUE);
        data.put(TIMESTAMP_KEY, TIMESTAMP_VALUE);
    }

    @Test
    public void testOfWithNullResultSet() throws Exception {
        Row row = Row.of(null);
        assertThat(row, is(nullValue()));
    }

    @Test
    public void testOfWithValidResultSet() throws Exception {
        Row row = Row.of(new MockResultSet(data));
        assertThat(row, hasEntry(ID_KEY, ID_VALUE));
        assertThat(row, hasEntry(USERNAME_KEY, USERNAME_VALUE));
        assertThat(row, hasEntry(LAST_NAME_KEY, LAST_NAME_VALUE));
        assertThat(row, hasEntry(FIRST_NAME_KEY, FIRST_NAME_VALUE));
        assertThat(row, hasEntry(DATE_KEY, DATE_VALUE_FORMATTED));
        assertThat(row, hasEntry(TIME_KEY, TIME_VALUE_FORMATTED));
        assertThat(row, hasEntry(TIMESTAMP_KEY, TIMESTAMP_VALUE_FORMATTED));
    }

    private static class MockMetaData extends RowSetMetaDataImpl {
        Map<String, String> data;
        List<String> keys;

        public MockMetaData(Map<String, String> data) {
            this.data = data;
            this.keys = new ArrayList<String>(data.keySet());
        }

        @Override
        public int getColumnCount() throws SQLException {
            return data.size();
        }

        @Override
        public String getColumnName(int column) throws SQLException {
            return keys.get(column - 1);
        }

        @Override
        public int getColumnType(int column) throws SQLException {
            if (getColumnName(column).equals(DATE_KEY)) {
                return Types.DATE;
            } else if (getColumnName(column).equals(TIME_KEY)) {
                return Types.TIME;
            } else if (getColumnName(column).equals(TIMESTAMP_KEY)) {
                return Types.TIMESTAMP;
            }
            return Types.VARCHAR;
        }
    }

    private static class MockResultSet extends JdbcRowSetImpl {
        Map<String, String> data;
        ResultSetMetaData metaData;

        public MockResultSet(Map<String, String> data) {
            this.data = data;
            metaData = new MockMetaData(data);
        }

        @Override
        public ResultSetMetaData getMetaData() throws SQLException {
            return metaData;
        }

        @Override
        public String getString(int column) throws SQLException {
            return data.get(getMetaData().getColumnName(column));
        }

        private long parseTemporal(int column, SimpleDateFormat format) throws SQLException, ParseException {
            return format.parse(data.get(getMetaData().getColumnName(column))).getTime();
        }

        @Override
        public Date getDate(int column) throws SQLException {
            try {
                return new Date(parseTemporal(column, new SimpleDateFormat("yyyy.MM.dd")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Time getTime(int column) throws SQLException {
            try {
                return new Time(parseTemporal(column, new SimpleDateFormat("HH.mm.ss")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Timestamp getTimestamp(int column) throws SQLException {
            try {
                return new Timestamp(parseTemporal(column, new SimpleDateFormat("yyyy.MM.dd HH.mm.ss")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}