package com.demo.example.truecdexample.steps;

import com.demo.example.truecdexample.model.Book;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GetBookStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CucumberTestConfig config;

    private Book savedBook;
    private ResponseEntity<Book> response;

    @Given("there is an existing book with following details")
    public void there_is_an_existing_book_with_following_details(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        Map<String, String> bookData = rows.get(0);

        Book book = new Book();
        book.setTitle(bookData.get("title"));
        book.setAuthor(bookData.get("author"));
        book.setIsbn(bookData.get("isbn"));
        book.setPrice(Double.parseDouble(bookData.get("price")));

        // Save the book first
        ResponseEntity<Book> createResponse = restTemplate.postForEntity(
                config.getBaseUrl() + "/api/books",
                book,
                Book.class);

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        savedBook = createResponse.getBody();
        assertNotNull(savedBook);
        assertNotNull(savedBook.getId());
    }

    @When("I send a request to get the book by its ID")
    public void i_send_a_request_to_get_the_book_by_its_id() {
        response = restTemplate.getForEntity(
                config.getBaseUrl() + "/api/books/" + savedBook.getId(),
                Book.class);
    }

    @When("I send a request to get a book with ID {string}")
    public void i_send_a_request_to_get_a_book_with_id(String id) {
        response = restTemplate.getForEntity(
                config.getBaseUrl() + "/api/books/" + id,
                Book.class);
    }

    @Then("the response status should be OK")
    public void the_response_status_should_be_ok() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Then("the response status should be NOT_FOUND")
    public void the_response_status_should_be_not_found() {
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Then("the response should include the book details")
    public void the_response_should_include_the_book_details() {
        Book retrievedBook = response.getBody();
        assertNotNull(retrievedBook);
        assertEquals(savedBook.getId(), retrievedBook.getId());
        assertEquals(savedBook.getTitle(), retrievedBook.getTitle());
        assertEquals(savedBook.getAuthor(), retrievedBook.getAuthor());
        assertEquals(savedBook.getIsbn(), retrievedBook.getIsbn());
        assertEquals(savedBook.getPrice(), retrievedBook.getPrice());
    }
}