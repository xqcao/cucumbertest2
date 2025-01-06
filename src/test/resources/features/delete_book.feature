Feature: Delete Book
  As a librarian
  I want to delete books from the system
  So that I can remove outdated or unwanted books

  Scenario: Successfully delete an existing book
    Given there is an existing book in the system with following details
      | title          | author           | isbn           | price |
      | The Great Gatsby | F. Scott Fitzgerald | 978-0743273565 | 9.99  |
    When I send a request to delete the book
    Then the delete operation should be successful
    And the book should no longer exist in the system

  Scenario: Delete a non-existent book
    When I try to delete a book with ID "999"
    Then the delete operation should return not found status 