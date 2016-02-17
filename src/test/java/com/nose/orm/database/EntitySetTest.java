package com.nose.orm.database;

import com.nose.model.Address;
import com.nose.model.User;
import com.nose.utils.DBUnitTest;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Daniel on 03.02.2016.
 */
public class EntitySetTest extends DBUnitTest {

    private EntitySet<User> userEntitySet;
    private EntitySet<Address> addressEntitySet;


    public EntitySetTest(String name) throws Exception {
        super(name);
    }


    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream("EntitySetTest.xml")
        );
    }

    @Before
    public void setUp() throws Exception {
        userEntitySet = new EntitySet<User>(User.class);
        addressEntitySet = new EntitySet<Address>(Address.class);
        super.setUp();
    }

    @Test
    public void testGenerateTree() throws Exception {
        userEntitySet.populateFromSelect(getConnection().getConnection(), "select * from user");

        List<User> users = userEntitySet.generateTree();

        assertThat(users.size(), is(equalTo(1)));
        User user = users.get(0);
        assertThat(user.getId(), is(equalTo(1L)));
        assertThat(user.getLastName(), is(equalTo("Kobler")));
        assertThat(user.getFirstName(), is(equalTo("Daniel")));
        assertThat(user.getAge(), is(equalTo(37)));
        Calendar c1 = GregorianCalendar.getInstance();
        c1.setTimeZone(TimeZone.getTimeZone("GMT"));
        c1.set(1978, 7, 22, 0, 0, 0);  // August 22nd 1978
        c1.set(Calendar.MILLISECOND, 0);
        assertThat(user.getBirthDate(), equalTo(c1.getTime()));
    }



    @Test
    public void testGenerateTree2() throws Exception {
        addressEntitySet.populateFromSelect(getConnection().getConnection(), "select * from address");

        List<Address> addresses = addressEntitySet.generateTree();

        assertThat(addresses.size(), is(equalTo(1)));
        Address address = addresses.get(0);
        assertThat(address.getId(), is(equalTo(1L)));
        assertThat(address.getStreet(), is(equalTo("Street 1")));
        assertThat(address.getZip(), is(equalTo("123")));
        assertThat(address.getCity(), is(equalTo("Geneva")));
        assertThat(address.getCountry(), is(equalTo("Suisse")));
    }

}