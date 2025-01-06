package com.demo.example.truecdexample.steps;

import com.demo.example.truecdexample.model.Book;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateBookStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CucumberTestConfig config;

    private Book existingBook;
    private ResponseEntity<Book> response;

    @Given("there is a book in the system with following details")
    public void thereIsABookInSystemWithFollowingDetails(DataTable dataTable) {
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

    @When("I update the book with new details")
    public void updateBookWithNewDetails(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        Map<String, String> bookData = rows.get(0);

        Book updatedBook = new Book();
        updatedBook.setTitle(bookData.get("title"));
        updatedBook.setAuthor(bookData.get("author"));
        updatedBook.setIsbn(bookData.get("isbn"));
        updatedBook.setPrice(Double.parseDouble(bookData.get("price")));

        response = restTemplate.exchange(
                config.getBaseUrl() + "/api/books/" + existingBook.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updatedBook),
                Book.class);
    }

    @When("I try to update a book with ID {string} with following details")
    public void tryToUpdateBookWithIdAndDetails(String id, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        Map<String, String> bookData = rows.get(0);

        Book updatedBook = new Book();
        updatedBook.setTitle(bookData.get("title"));
        updatedBook.setAuthor(bookData.get("author"));
        updatedBook.setIsbn(bookData.get("isbn"));
        updatedBook.setPrice(Double.parseDouble(bookData.get("price")));

        response = restTemplate.exchange(
                config.getBaseUrl() + "/api/books/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(updatedBook),
                Book.class);
    }

    @Then("the response status code should be OK")
    public void verifyResponseStatusIsOk() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Then("the response status code should be NOT_FOUND")
    public void verifyResponseStatusIsNotFound() {
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Then("the book should be updated with new details")
    public void verifyBookIsUpdatedWithNewDetails() {
        Book updatedBook = response.getBody();
        assertNotNull(updatedBook);
        assertEquals(existingBook.getId(), updatedBook.getId());

        // Verify the book was actually updated in the database
        ResponseEntity<Book> getResponse = restTemplate.getForEntity(
                config.getBaseUrl() + "/api/books/" + existingBook.getId(),
                Book.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Book retrievedBook = getResponse.getBody();
        assertNotNull(retrievedBook);

        // Compare all fields
        assertEquals(updatedBook.getTitle(), retrievedBook.getTitle());
        assertEquals(updatedBook.getAuthor(), retrievedBook.getAuthor());
        assertEquals(updatedBook.getIsbn(), retrievedBook.getIsbn());
        assertEquals(updatedBook.getPrice(), retrievedBook.getPrice());
    }
}