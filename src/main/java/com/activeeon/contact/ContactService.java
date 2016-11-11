package com.activeeon.contact;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jws.WebService;
import java.util.List;

/**
 * Created by Clement Caron on 09/11/2016.
 */
@WebService(endpointInterface = "com.activeeon.contact.ContactInterface")
public class ContactService implements ContactInterface {
    private static final Logger LOGGER = LogManager.getLogger(ContactService.class.getName());
    private final DatabaseConnector dataBaseConnector = new DatabaseConnector();
    private final ContactValidator validator = new ContactValidator();

    @Override
    public boolean createContact(Contact contact) {
        LOGGER.debug("Getting a new Contact");

        List<String> violations = validator.validateContact(contact);
        if (violations.size() == 0) {
            return dataBaseConnector.createContact(contact);
        } else {
            LOGGER.error("Contact has violations");
            violations.forEach(path -> {
                LOGGER.debug("{} field is invalid", path);
            });

            throw new IllegalArgumentException(violations.toString());
        }
    }

    @Override
    public boolean updateContact(Contact contact) {
        LOGGER.debug("Getting a Contact update");

        List<String> violations = validator.validateContact(contact);
        if (violations.size() == 0) {
            return dataBaseConnector.updateContact(contact);
        } else {
            LOGGER.error("Report has violations");
            violations.forEach(path -> {
                LOGGER.debug("{} field is invalid", path);
            });

            throw new IllegalArgumentException(violations.toString());
        }
    }

    @Override
    public boolean deleteContact(Contact contact) {
        LOGGER.debug("Getting a Contact deletion");

        List<String> violations = validator.validateContact(contact);
        if (violations.size() == 0) {
            return dataBaseConnector.deleteContact(contact);
        } else {
            LOGGER.error("Report has violations");
            violations.forEach(path -> {
                LOGGER.debug("{} field is invalid", path);
            });

            throw new IllegalArgumentException(violations.toString());
        }
    }

    @Override
    public List<Contact> getAllContacts() {
        LOGGER.debug("Getting a AllContacts request");
        return dataBaseConnector.getAllContacts();
    }

    @Override
    public List<Contact> getContactsByName(String name) {
        LOGGER.debug("Getting a ContactsByName request");
        return dataBaseConnector.getContactsByName(name);
    }

    @Override
    public List<Contact> getContactsBySubName(String subName) {
        LOGGER.debug("Getting a ContactsBySubName request");
        return dataBaseConnector.getContactsBySubName(subName);
    }

    @Override
    public Contact getContactByPhoneNumber(String phoneNumber) {
        LOGGER.debug("Getting a ContactByPhoneNumber request");
        return dataBaseConnector.getContactByPhoneNumber(phoneNumber);
    }
}

