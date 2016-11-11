package com.activeeon.contact;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Clement Caron on 09/11/2016.
 */
public class DatabaseTest {
    @Test
    public void fillingDatabaseTest() {
        DatabaseConnector dbc = new DatabaseConnector();
        dbc.createContact(new Contact("15", "Samu", "Hopital"));
        dbc.createContact(new Contact("17", "Police", "Commissariat"));
        dbc.createContact(new Contact("18", "Pompier", "Caserne"));
        assertEquals(3, dbc.getAllContacts().size());

        dbc.updateContact(new Contact("15", "Smur", "Hopital"));
        List<Contact> contacts = dbc.getContactsByName("Smur");
        assertEquals(1, contacts.size());
        assertEquals("15", contacts.get(0).getPhoneNumber());

        contacts = dbc.getContactsBySubName("Po");
        assertEquals(2, contacts.size());

        contacts = dbc.getContactsBySubName("Pom");
        assertEquals(1, contacts.size());
        assertEquals("18", contacts.get(0).getPhoneNumber());

        assertEquals("Commissariat", dbc.getContactByPhoneNumber("17").getAddress());

        dbc.deleteContact(new Contact("18", "a", "b"));
        assertEquals(2, dbc.getAllContacts().size());
    }
}
