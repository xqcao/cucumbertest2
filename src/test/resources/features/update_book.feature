Feature: Update Book
  As a librarian
  I want to update book information
  So that I can maintain accurate book details

  Scenario: Successfully update an existing book
    Given there is a book in the system with following details
      | title          | author           | isbn           | price |
      | The Great Gatsby | F. Scott Fitzgerald | 978-0743273565 | 9.99  |
    When I update the book with new details
      | title          | author           | isbn           | price |
      | The Great Gatsby (Updated) | F. Scott Fitzgerald | 978-0743273565 | 19.99 |
    Then the update operation should be successful
    And the book should be updated with new details

  Scenario: Update a non-existent book
    When I try to update a book with ID "999" with following details
      | title          | author           | isbn           | price |
      | Non Existent Book | Unknown Author | 000-0000000000 | 29.99 |
    Then the update operation should return not found status 