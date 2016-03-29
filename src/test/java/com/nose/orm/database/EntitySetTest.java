package com.nose.orm.database;

import com.nose.model.Address;
import com.nose.model.Invoice;
import com.nose.model.InvoiceStatus;
import com.nose.model.User;
import com.nose.utils.DBUnitTest;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.nose.utils.hamcrest.matchers.model.Model.*;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;

/**
 * Created by Daniel on 03.02.2016.
 */
public class EntitySetTest extends DBUnitTest {


    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");


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

    @Test
    public void testGenerateTreeForUsers() throws Exception {
        EntitySet<User> userEntitySet = EntitySet.create(User.class);
        userEntitySet.populateFromSelect(getConnection().getConnection(), "select * from user");

        List<User> users = userEntitySet.generateTree();

        assertThat(users, contains(
                user(1l, "Kobler", "Daniel", format.parse("1978-08-22T00:00:00.000+0000"),
                        address(1L, "Street 1", "123", "Geneva", "Suisse"),
                        contains(
                            invoice(2l, InvoiceStatus.PAID, format.parse("2015-06-03T09:40:00.000+0000"), contains(
                                    invoiceItem(3l, 1, new BigDecimal("10"), "Item 1", "Item 1 description"),
                                    invoiceItem(4l, 2, new BigDecimal("20"), "Item 2", "Item 2 description"),
                                    invoiceItem(5l, 3, new BigDecimal("30"), "Item 3", "Item 3 description")
                            )),
                            invoice(1l, InvoiceStatus.OPEN, format.parse("2015-01-01T10:00:00.000+0000"), contains(
                                    invoiceItem(1l,  5, new BigDecimal("10"), "Item 1", "Item 1 description"),
                                    invoiceItem(2l, 10, new BigDecimal("20"), "Item 2", "Item 2 description")
                            ))
                    )
                ),
                user(2l, "LastName2", "FirstName2", format.parse("1980-05-29T00:00:00.000+0000"),
                        address(2L, "Street 2", "456", "Paris", "France"),
                        contains(
                                invoice(3l, InvoiceStatus.OPEN, format.parse("2000-01-01T00:00:00.000+0000"), contains(
                                        invoiceItem(6l, 100, new BigDecimal("1"), "Item 1", "Item 1 description")
                                ))
                        )
                )
        ));
    }



    @Test
    public void testGenerateTreeForInvoicesInGivenOrder() throws Exception {
        EntitySet<Invoice> invoiceEntitySet = EntitySet.create(Invoice.class);
        invoiceEntitySet.populateFromSelect(getConnection().getConnection(), "select * from invoice order by status, date");

        List<Invoice> invoices = invoiceEntitySet.generateTree();

        assertThat(invoices, contains(
            invoice(3l, InvoiceStatus.OPEN, format.parse("2000-01-01T00:00:00.000+0000"), contains(
                    invoiceItem(6l,  100, new BigDecimal("1"), "Item 1", "Item 1 description")
            )),
            invoice(1l, InvoiceStatus.OPEN, format.parse("2015-01-01T10:00:00.000+0000"), contains(
                    invoiceItem(1l,  5, new BigDecimal("10"), "Item 1", "Item 1 description"),
                    invoiceItem(2l, 10, new BigDecimal("20"), "Item 2", "Item 2 description")
            )),
            invoice(2l, InvoiceStatus.PAID, format.parse("2015-06-03T09:40:00.000+0000"), contains(
                    invoiceItem(3l, 1, new BigDecimal("10"), "Item 1", "Item 1 description"),
                    invoiceItem(4l, 2, new BigDecimal("20"), "Item 2", "Item 2 description"),
                    invoiceItem(5l, 3, new BigDecimal("30"), "Item 3", "Item 3 description")
            ))
        ));
    }



    @Test
    public void testGenerateTreeForAddresses() throws Exception {
        EntitySet<Address> addressEntitySet = EntitySet.create(Address.class);
        addressEntitySet.populateFromSelect(getConnection().getConnection(), "select * from personal_address");

        List<Address> addresses = addressEntitySet.generateTree();

        assertThat(addresses, contains(
                address(1L, "Street 1", "123", "Geneva", "Suisse"),
                address(2L, "Street 2", "456", "Paris", "France")
        ));
    }

}