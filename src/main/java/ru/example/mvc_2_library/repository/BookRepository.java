package ru.example.mvc_2_library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.mvc_2_library.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}