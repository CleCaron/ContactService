package com.activeeon.contact;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Clement Caron on 09/11/2016.
 */
class DatabaseConnector {
    private static final Logger LOGGER = LogManager.getLogger(DatabaseConnector.class.getName());
    private final String databaseURL;

    DatabaseConnector() {
        try {
            Class.forName("org.h2.Driver").newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            LOGGER.error("Couldn't load JDBC driver", e);
        }

        Properties prop = new Properties();
        try (InputStream is = DatabaseConnector.class.getClassLoader().getResourceAsStream("database.properties.xml")) {
            prop.loadFromXML(is);
        } catch (IOException e) {
            LOGGER.error("Can't find database.properties.xml");
            throw new IllegalArgumentException("Can't find database.properties.xml", e);
        }

        databaseURL = prop.getProperty("database.url");

        try (Connection dataBaseConnection = DriverManager.getConnection(databaseURL)){
            String SQL = "CREATE TABLE IF NOT EXISTS Contact ( phoneNumber VARCHAR(20) PRIMARY KEY NOT NULL,name VARCHAR(100),address VARCHAR(100));" +
                    "DELETE FROM Contact;";
            PreparedStatement statement = dataBaseConnection.prepareStatement(SQL);
            statement.execute();
            dataBaseConnection.close();
        } catch (SQLException e) {
            LOGGER.error("Error while connecting to the database", e);
        }
    }

    boolean createContact(Contact contact) {
        boolean result = false;
        String SQL = "INSERT INTO Contact VALUES ('" + contact.getPhoneNumber() + "', '" + contact.getName() + "', '" + contact.getAddress() + "');";

        try(Connection dataBaseConnection = DriverManager.getConnection(databaseURL)) {
            PreparedStatement statement = dataBaseConnection.prepareStatement(SQL);
            result = statement.executeUpdate() != 0;
            dataBaseConnection.close();
        } catch (SQLException e) {
            LOGGER.error("Error while creating contact", e);
        }

        return result;
    }

    boolean updateContact(Contact contact) {
        boolean result = false;
        String SQL = "UPDATE Contact SET (name ,address) = ('" + contact.getName() + "', '" + contact.getAddress() + "')" +
                " WHERE phoneNumber  = '" + contact.getPhoneNumber() + "';";

        try(Connection dataBaseConnection = DriverManager.getConnection(databaseURL)) {
            PreparedStatement statement = dataBaseConnection.prepareStatement(SQL);
            result = statement.executeUpdate() != 0;
            dataBaseConnection.close();
        } catch (SQLException e) {
            LOGGER.error("Error while updating contact", e);
        }

        return result;
    }

    boolean deleteContact(Contact contact) {
        boolean result = false;
        String SQL = "DELETE FROM Contact WHERE phoneNumber = '" + contact.getPhoneNumber() + "';";

        try(Connection dataBaseConnection = DriverManager.getConnection(databaseURL)) {
            PreparedStatement statement = dataBaseConnection.prepareStatement(SQL);
            result = statement.executeUpdate() != 0;
            dataBaseConnection.close();
        } catch (SQLException e) {
            LOGGER.error("Error while deleting contact", e);
        }

        return result;
    }

    List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        String SQL = "SELECT * FROM Contact;";

        try(Connection dataBaseConnection = DriverManager.getConnection(databaseURL)) {
            PreparedStatement statement = dataBaseConnection.prepareStatement(SQL);
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                contacts.add(new Contact(rs.getString("phoneNumber"),
                        rs.getString("name"),
                        rs.getString("address")));
            }
            dataBaseConnection.close();
        } catch (SQLException e) {
            LOGGER.error("Error while requesting all contacts", e);
        }

        return contacts;
    }


    List<Contact> getContactsByName(String name) {
        List<Contact> contacts = new ArrayList<>();

        String SQL = "SELECT * FROM Contact WHERE name = '" + name + "';";

        try(Connection dataBaseConnection = DriverManager.getConnection(databaseURL)) {
            PreparedStatement statement = dataBaseConnection.prepareStatement(SQL);
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                contacts.add(new Contact(rs.getString("phoneNumber"),
                        rs.getString("name"),
                        rs.getString("address")));
            }
            dataBaseConnection.close();
        } catch (SQLException e) {
            LOGGER.error("Error while requesting contacts by name", e);
        }

        return contacts;
    }

    List<Contact> getContactsBySubName(String subName) {
        List<Contact> contacts = new ArrayList<>();

        String SQL = "SELECT * FROM Contact WHERE name regexp '.*" + subName + ".*';";

        try(Connection dataBaseConnection = DriverManager.getConnection(databaseURL)) {
            PreparedStatement statement = dataBaseConnection.prepareStatement(SQL);
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                contacts.add(new Contact(rs.getString("phoneNumber"),
                        rs.getString("name"),
                        rs.getString("address")));
            }
            dataBaseConnection.close();
        } catch (SQLException e) {
            LOGGER.error("Error while requesting contacts by subName", e);
        }

        return contacts;
    }

    Contact getContactByPhoneNumber(String phoneNumber) {
        Contact contact = null;
        String SQL = "SELECT * FROM Contact WHERE phoneNumber = '" + phoneNumber + "';";

        try(Connection dataBaseConnection = DriverManager.getConnection(databaseURL)) {
            PreparedStatement statement = dataBaseConnection.prepareStatement(SQL);
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            if (rs.next()) {
                contact = new Contact(rs.getString("phoneNumber"),
                        rs.getString("name"),
                        rs.getString("address"));
            }
            dataBaseConnection.close();
        } catch (SQLException e) {
            LOGGER.error("Error while requesting contact by phone number", e);
        }

        return contact;
    }
}
