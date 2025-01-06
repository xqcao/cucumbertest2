package com.demo.example.truecdexample.steps;

import com.demo.example.truecdexample.model.Book;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteBookStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CucumberTestConfig config;

    private Book existingBook;
    private ResponseEntity<Void> deleteResponse;

    @Given("there is an existing book in the system with following details")
    public void createBookIntoSystem(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        Map<String, String> bookData = rows.get(0);

        Book book = new Book();
        book.setTitle(bookData.get("title"));
        book.setAuthor(bookData.get("author"));
        book.setIsbn(bookData.get("isbn"));
        book.setPrice(Double.parseDouble(bookData.get("price")));

        // Create the book first
        ResponseEntity<Book> createResponse = restTemplate.postForEntity(
                config.getBaseUrl() + "/api/books",
                book,
                Book.class);

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        existingBook = createResponse.getBody();
        assertNotNull(existingBook);
        assertNotNull(existingBook.getId());
    }

    @When("I send a request to delete the book")
    public void deleteAExistingBook() {
        deleteResponse = restTemplate.exchange(
                config.getBaseUrl() + "/api/books/" + existingBook.getId(),
                HttpMethod.DELETE,
                null,
                Void.class);
    }

    @When("I try to delete a book with ID {string}")
    public void deleteABookNonExistentBook(String id) {
        deleteResponse = restTemplate.exchange(
                config.getBaseUrl() + "/api/books/" + id,
                HttpMethod.DELETE,
                null,
                Void.class);
    }

    @Then("the book should no longer exist in the system")
    public void verifyBookShouldNoLongerExists() {
        // Try to get the deleted book
        ResponseEntity<Book> getResponse = restTemplate.getForEntity(
                config.getBaseUrl() + "/api/books/" + existingBook.getId(),
                Book.class);

        // Verify that the book is not found
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Then("the delete operation should be successful")
    public void verifyDeleteOperationSuccessful() {
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
    }

    @Then("the delete operation should return not found status")
    public void verifyDeleteOperationNotFound() {
        assertEquals(HttpStatus.NOT_FOUND, deleteResponse.getStatusCode());
    }
}