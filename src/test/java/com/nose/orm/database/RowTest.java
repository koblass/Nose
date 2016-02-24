package com.nose.orm.database;

import com.sun.rowset.JdbcRowSetImpl;
import org.junit.Test;

import javax.sql.rowset.RowSetMetaDataImpl;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by Daniel on 14.02.2016.
 */
public class RowTest {
    Map<String, String> data = new LinkedHashMap<String, String>();

    public RowTest() {
        data.put("id", "1");
        data.put("username", "jdoe");
        data.put("last_name", "joe");
        data.put("first_name", "john");
        data.put("birthdate", "1970.01.01");
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
            return keys.get(column-1);
        }
        @Override
        public int getColumnType(int column) throws SQLException {
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
    }



    @Test
    public void testOfWithNullResultSet() throws Exception {
        Row row = Row.of(null);
        assertThat(row, is(nullValue()));
    }



    @Test
    public void testOfWithValidResultSet() throws Exception {
        Row row = Row.of(new MockResultSet(data));
        assertThat(row.size(), is(equalTo(5)));
        assertThat(row.keySet(), contains(data.keySet().toArray()));
        assertThat(row.values(), contains(data.values().toArray()));
    }
}