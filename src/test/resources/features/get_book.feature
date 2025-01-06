Feature: Get Book
  As a user
  I want to retrieve book information
  So that I can view book details

  Scenario: Successfully get a book by ID
    Given there is an existing book with following details
      | title          | author           | isbn           | price |
      | The Great Gatsby | F. Scott Fitzgerald | 978-0743273565 | 9.99  |
    When I send a request to get the book by its ID
    Then the response status should be OK
    And the response should include the book details

  Scenario: Get a non-existent book
    When I send a request to get a book with ID "999"
    Then the response status should be NOT_FOUND 