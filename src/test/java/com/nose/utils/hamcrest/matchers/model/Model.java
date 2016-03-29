package com.nose.utils.hamcrest.matchers.model;

import com.nose.model.*;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Daniel on 23.02.2016.
 */
public class Model {


    /**
     * Hamcrest matcher
     */
    public static Matcher<User> user(final Long id, final String lastName, final String firstName, final Date birthDate, final Matcher<? extends Address> addressMatchers, final Matcher<java.lang.Iterable<? extends Invoice>> invoiceMatchers) {

        return new TypeSafeMatcher<User>() {
            @Override
            public boolean matchesSafely(final User model) {
                return  model.getId().equals(id) &&
                        model.getLastName().equals(lastName) &&
                        model.getFirstName().equals(firstName) &&
                        model.getBirthDate().equals(birthDate) &&
                        addressMatchers.matches(model.getAddress()) &&
                        invoiceMatchers.matches(model.getInvoices());
            }

            @Override
            public void describeTo(final Description desc) {
                desc.appendText("id ").appendValue(id).appendText(" and lastName ").appendValue(lastName)
                        .appendText(" and firstName ").appendValue(firstName).appendText(" and birthDate ")
                        .appendValue(birthDate)
                        .appendText(" and address is ").appendDescriptionOf(addressMatchers)
                        .appendText(" and invoices containing ").appendDescriptionOf(invoiceMatchers);
            }
        };
    }


    /**
     * Hamcrest matcher
     */
    public static Matcher<Address> address(final Long id, final String street, final String zip, final String city, final String country) {

        return new TypeSafeMatcher<Address>() {
            @Override
            public boolean matchesSafely(final Address model) {
                return  model.getId().equals(id) &&
                        model.getStreet().equals(street) &&
                        model.getZip().equals(zip) &&
                        model.getCity().equals(city) &&
                        model.getCountry().equals(country);
            }

            @Override
            public void describeTo(final Description desc) {
                desc.appendText("id ").appendValue(id).appendText(" and street ").appendValue(street)
                        .appendText(" and zip ").appendValue(zip).appendText(" and city ")
                        .appendValue(city).appendText(" and country ").appendValue(country);
            }
        };
    }


    /**
     * Hamcrest matcher
     */
    public static Matcher<Invoice> invoice(final Long id, final InvoiceStatus status, final Date date, final Matcher<java.lang.Iterable<? extends InvoiceItem>> invoiceItemsMatchers) {

        return new TypeSafeMatcher<Invoice>() {
            @Override
            public boolean matchesSafely(final Invoice model) {
                return model.getId().equals(id) && model.getStatus().equals(status) && model.getDate().equals(date) && invoiceItemsMatchers.matches(model.getItems());
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("id ").appendValue(id).appendText(" and status ").appendValue(status).appendText(" and date ").appendValue(date).appendText(" and items containing ").appendDescriptionOf(invoiceItemsMatchers);
            }
        };
    }


    /**
     * Hamcrest matcher
     */
    public static Matcher<InvoiceItem> invoiceItem(final Long id, final int quantity, final BigDecimal price, final String name, final String description) {

        return new TypeSafeMatcher<InvoiceItem>() {
            @Override
            public boolean matchesSafely(final InvoiceItem model) {
                return  model.getId().equals(id) &&
                        model.getQuantity() == quantity &&
                        model.getPrice().equals(price) &&
                        model.getName().equals(name) &&
                        model.getDescription().equals(description);
            }

            @Override
            public void describeTo(final Description desc) {
                desc.appendText("id ").appendValue(id).appendText(" and quantity ").appendValue(quantity)
                        .appendText(" and price ").appendValue(price).appendText(" and name ")
                        .appendValue(name).appendText(" and description ").appendValue(description);
            }
        };
    }
}
