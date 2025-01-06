package com.demo.example.truecdexample.steps;

import com.demo.example.truecdexample.model.Book;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AddBookStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CucumberTestConfig config;

    private Book bookToAdd;
    private ResponseEntity<Book> response;

    @Given("I have a new book with following details")
    public void i_have_a_new_book_with_following_details(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        Map<String, String> bookData = rows.get(0);

        bookToAdd = new Book();
        bookToAdd.setTitle(bookData.get("title"));
        bookToAdd.setAuthor(bookData.get("author"));
        bookToAdd.setIsbn(bookData.get("isbn"));
        bookToAdd.setPrice(bookData.get("price") != null ? Double.parseDouble(bookData.get("price")) : null);
    }

    @When("I send a request to add the book")
    public void i_send_a_request_to_add_the_book() {
        response = restTemplate.postForEntity(
                config.getBaseUrl() + "/api/books",
                bookToAdd,
                Book.class);
    }

    @Then("the book should be successfully created")
    public void the_book_should_be_successfully_created() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Then("the response should contain the book details")
    public void the_response_should_contain_the_book_details() {
        Book createdBook = response.getBody();
        assertNotNull(createdBook);
        assertNotNull(createdBook.getId());
        assertEquals(bookToAdd.getTitle(), createdBook.getTitle());
        assertEquals(bookToAdd.getAuthor(), createdBook.getAuthor());
        assertEquals(bookToAdd.getIsbn(), createdBook.getIsbn());
        assertEquals(bookToAdd.getPrice(), createdBook.getPrice());
    }

    @Then("the request should be rejected with bad request status")
    public void the_request_should_be_rejected_with_bad_request_status() {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}