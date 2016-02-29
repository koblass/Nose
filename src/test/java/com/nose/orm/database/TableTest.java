package com.nose.orm.database;


import org.junit.Before;
import org.junit.Test;

import static com.nose.utils.hamcrest.matchers.database.ColumnValues.hasEntry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;

/**
 * Created by Daniel on 29.02.2016.
 */
public class TableTest {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String AGE = "age";

    private Table table;

    @Before
    public void setUp() {
        table = new Table();
        table.add(createRow("1", "name1", "11"));
        table.add(createRow("2", "name2", "12"));
        table.add(createRow("3", "name3", "13"));
        table.add(createRow("4", "name4", "14"));
    }

    private Row createRow(String id, String name, String age) {
        Row row = new Row();
        row.put(ID, id);
        row.put(NAME, name);
        row.put(AGE, age);
        return row;
    }


    @Test
    public void testGetColumnValues() {
        assertThat(table.getColumnValues(), hasEntry(ID, hasItems("1", "2", "3", "4")));
        assertThat(table.getColumnValues(), hasEntry(NAME, hasItems("name1", "name2", "name3", "name4")));
        assertThat(table.getColumnValues(), hasEntry(AGE, hasItems("11", "12", "13", "14")));
    }

}