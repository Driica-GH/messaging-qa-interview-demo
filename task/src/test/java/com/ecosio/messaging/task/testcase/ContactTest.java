package com.ecosio.messaging.task.testcase;

import com.ecosio.messaging.task.model.Contact;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

// TODO: have a look at all the existing tests (some may need fixing or can be implemented in a
//  better way, you are not expected to fix them, but think about why and how you would address
//  these issues and we'll talk about them in the next interview)

// TODO: think of a few different tests (including/excluding the ones here)
//  you would implement and why
@Slf4j
public class ContactTest extends BaseTest {

    @Test
    void allContacts() throws IOException {

        // get all available contacts
        List<Contact> contacts = getAllContacts();

        log.info("all contacts: {}", contacts);

        // number of contacts is expected to stay the same
        assertThat(contacts.size())
                .as("number of contacts")
                .isEqualTo(3);

    }

    @Test
    void updateContact() throws IOException {

        // get specific contact
        List<Contact> contactsBefore = getContactByFirstname("Testa");

        assertThat(contactsBefore.size())
                .as("number of contacts before update")
                .isOne();

        // update previously retrieved contact to this
        Contact updatedContact = new Contact(
                contactsBefore.get(0).getId(),
                "abc",
                "def"
        );

        // log
        log.info("updatedContact", updatedContact);
        Contact contactBefore = contactsBefore.getFirst();
        log.info("contactBefore", contactBefore);
        updateContact(contactBefore, updatedContact);

        // verify that a new updated contact is on the list
        List<Contact> updatedContacts = getAllContacts()
                .stream()
                .filter(contact -> contact.getFirstname().equals(updatedContact.getFirstname())
                        && contact.getLastname().equals(updatedContact.getLastname())).toList();

        assertThat(updatedContacts.size())
                .as("number of contacts after update")
                .isOne();

        // verify that the old contact is no longer on the list
        List<Contact> beforeContacts = getAllContacts()
                .stream()
                .filter(contact -> contact.getFirstname().equals(contactBefore.getFirstname())
                        && contact.getLastname().equals(contactBefore.getLastname())).toList();

        assertThat(beforeContacts.size())
                .as("number of contacts after update")
                .isZero();

        // test clean up
        updateContact(updatedContact, contactBefore);


        // TODO: implement remaining testcase

    }

    @Test
    void getContactByFirstname() throws IOException {

        // TODO: get ALL contacts with the string "name" in it and add assertions
        List<Contact> contacts = getContactByFirstname("name");
        log.info("all contacts: {}", contacts);

        // This test will always fail, given that the method findByFirstName in
        // the ContactController.java uses .findFirst method - Justyna
        assertThat(contacts.size())
                .as("number of contacts")
                .isEqualTo(2);
    }

    @Test
    void getContactByLastName() throws IOException {

        List<Contact> contacts = getContactByLastname("Testb");
        log.info("all contacts: {}", contacts);

        // This test will always fail, given that the method findByLastName in
        // the ContactController.java compares first name to last name instead - Justyna
        assertThat(contacts.size())
                .as("number of contacts")
                .isEqualTo(1);
    }

    @Test
    void addNewContact() throws IOException {

        Contact newContact = new Contact(4, "newFirstName", "newLastName");
        addContact(newContact);

        // verify that a new contact is on the list
        List<Contact> addedContacts = getAllContacts()
                .stream()
                .filter(contact -> contact.getFirstname().equals(newContact.getFirstname())
                        && contact.getLastname().equals(newContact.getLastname())
                        && contact.getId()== newContact.getId()).toList();

        // verify that one new contact has been added
        assertThat(addedContacts.size())
                .as("number of created contacts")
                .isOne();

        log.info("delete");
        deleteContact(newContact.getId());

        // get list of deleted contacts
        List<Contact> deletedContacts = getAllContacts()
                .stream()
                .filter(contact -> contact.getFirstname().equals(newContact.getFirstname())
                        && contact.getLastname().equals(newContact.getLastname())
                        && contact.getId()== newContact.getId()).toList();

        // verify that the contact was deleted
        assertThat(deletedContacts.size())
                .as("number of deleted contacts")
                .isZero();
    }

}
