package com.ecosio.messaging.task.testcase;

import com.ecosio.messaging.task.model.Contact;
import com.ecosio.messaging.task.util.HttpClientHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@Slf4j
public class BaseTest {

    protected final String appUnderTestBaseUrl = "http://localhost:18080/contact/";

    /**
     * gets all contacts where parameter <code>firstname</code> is a substring of the contacts firstname
     *
     * @param firstname
     * @return list of all matching contacts
     * @throws IOException
     */
    protected List<Contact> getContactByFirstname(String firstname) throws IOException {

        HttpGet httpGet = new HttpGet(appUnderTestBaseUrl + "firstname/" + firstname);
        return connectionHelper(httpGet);

    }

    /**
     * gets all contacts where parameter <code>lastname</code> is a substring of the contacts firstname
     *
     * @param lastname
     * @return list of all matching contacts
     * @throws IOException
     */
    protected List<Contact> getContactByLastname(String lastname) throws IOException {

        HttpGet httpGet = new HttpGet(appUnderTestBaseUrl + "lastname/" + lastname);
        return connectionHelper(httpGet);

    }

    /**
     * gets a list of all contacts stored of the app
     *
     * @return list of all contacts
     * @throws IOException
     */
    protected List<Contact> getAllContacts() throws IOException {

        HttpGet httpGet = new HttpGet(appUnderTestBaseUrl + "allContacts");
        return connectionHelper(httpGet);

    }

    /**
     * updates an existing contact
     *
     * @param currentContact contact to be updated
     * @param updatedContact contact to update the existing one to
     * @throws IOException
     */
    protected void updateContact(
            Contact currentContact,
            Contact updatedContact
    ) throws IOException {

        // TODO: implement a method updating a contact

        HttpPost httpPost = new HttpPost(appUnderTestBaseUrl + "createOrUpdateContact/" + currentContact.getId());
        {

            final String requestBody = """
                    {
                      "id":%1s,
                      "firstname":"%2s",
                      "lastname":"%3s"
                    }
                    """.formatted(updatedContact.getId(), updatedContact.getFirstname(), updatedContact.getLastname());
            log.info("Request body: {}", requestBody);
            final StringEntity entity = new StringEntity(requestBody);
            httpPost.setEntity(entity);
            // set headers
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            // execute request
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            log.info("Status: {}", response.getStatusLine().getStatusCode());
            log.info("Response: {}", responseBody);
        }

    }

    /**
     * adds a new contact
     *
     * @param newContact contact to be created
     * @throws IOException
     */
    protected void addContact(
            Contact newContact
            ) throws IOException {

        HttpPost httpPost = new HttpPost(appUnderTestBaseUrl + "createOrUpdateContact/" + newContact.getId());
        {

            final String requestBody = """
                    {
                      "id":%1s,
                      "firstname":"%2s",
                      "lastname":"%3s"
                    }
                    """.formatted(newContact.getId(), newContact.getFirstname(), newContact.getLastname());
            log.info("Request body: {}", requestBody);
            final StringEntity entity = new StringEntity(requestBody);
            httpPost.setEntity(entity);
            // set headers
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            // execute request
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            log.info("Status: {}", response.getStatusLine().getStatusCode());
            log.info("Response: {}", responseBody);
        }

    }

    /**
     * delete contact by parameter <code>id</code>
     *
     * @param id - id of the contact
     * @throws IOException
     */
    protected Contact deleteContact(Integer id) throws IOException {

        HttpDelete httpDelete = new HttpDelete(appUnderTestBaseUrl + id);
        return connectionHelperDelete(httpDelete);

    }

    /**
     * connection helper to abstract the "connection layer" from the "application layer"
     *
     * @param httpRequestBase
     * @return list of contacts based on the request
     * @throws IOException
     */
    private List<Contact> connectionHelper(HttpRequestBase httpRequestBase) throws IOException {

        try (CloseableHttpClient httpClient = HttpClientHelper.getHttpClientAcceptsAllSslCerts()) {
            try {

                ObjectMapper mapper = new ObjectMapper();
                String response = IOUtils.toString(
                        httpClient.execute(httpRequestBase)
                                .getEntity()
                                .getContent(),
                        StandardCharsets.UTF_8
                );
                log.info("connection helper {}", response);
                List<Contact> contacts = mapper.readValue(
                        response,
                        new TypeReference<List<Contact>>() {
                        }
                );
                return contacts;

            } finally {
                httpRequestBase.releaseConnection();
            }
        }

    }



    /**
     * connection helper to abstract the "connection layer" from the "application layer" for purpose of deleting record
     *
     * @param httpRequestBase
     * @return list of contacts based on the request
     * @throws IOException
     *
     * I was unable to make the original connectionHelper work for Delete endpoint, since it always expects a List (table)
     * in the response, so I duplicated it and adjusted to be able to handle TypeReference<Contact> in the response.
     */
    private Contact connectionHelperDelete(HttpRequestBase httpRequestBase) throws IOException {

        try (CloseableHttpClient httpClient = HttpClientHelper.getHttpClientAcceptsAllSslCerts()) {
            try {

                ObjectMapper mapper = new ObjectMapper();
                String response = IOUtils.toString(
                        httpClient.execute(httpRequestBase)
                                .getEntity()
                                .getContent(),
                        StandardCharsets.UTF_8
                );
                log.info("connection helper {}", response);
                Contact contacts = mapper.readValue(
                        response,
                        new TypeReference<Contact>() {
                        }
                );
                return contacts;

            } finally {
                httpRequestBase.releaseConnection();
            }
        }

    }

}
