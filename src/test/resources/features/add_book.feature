Feature: Add Book
  As a librarian
  I want to add a new book
  So that I can keep track of my book inventory

  Scenario: Successfully add a new book
    Given I have a new book with following details
      | title          | author           | isbn           | price |
      | The Great Gatsby | F. Scott Fitzgerald | 978-0743273565 | 9.99  |
    When I send a request to add the book
    Then the book should be successfully created
    And the response should contain the book details

  Scenario: Add book with missing required fields
    Given I have a new book with following details
      | title | author | isbn | price |
      |       |        |      | 9.99  |
    When I send a request to add the book
    Then the request should be rejected with bad request status 