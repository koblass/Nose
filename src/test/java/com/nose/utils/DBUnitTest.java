package com.nose.utils;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Statement;

/**
 * Created by Daniel on 08.02.2016.
 */
public abstract class DBUnitTest extends DBTestCase {

    public DBUnitTest(String name) throws Exception {
        super(name);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "org.hsqldb.jdbcDriver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:hsqldb:mem:sample");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "sa");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "");
        // System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA, "" );

        executeDDL(getConnection(), "Model.ddl");
    }

    protected void executeDDL(IDatabaseConnection databaseConnection, String filename) {
        try {
            Statement st = databaseConnection.getConnection()
                    .createStatement();
            st.execute(loadDDL(filename));
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            try {
                getDatabaseTester().closeConnection(databaseConnection);
            } catch (Exception e) {
                // ignore
            }
        }
    }

    protected String loadDDL(String filename) throws IOException {
        InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(filename);
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuffer buffer = new StringBuffer();
        String line = null;
        String EOL = System.getProperty("line.separator");
        while ((line = r.readLine()) != null) {
            if (!line.trim().equals("") && !line.startsWith("#")) {
                buffer.append(line + EOL);
            }
        }
        in.close();
        return buffer.toString();
    }

}
