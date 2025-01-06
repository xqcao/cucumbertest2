package com.demo.example.truecdexample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.example.truecdexample.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
