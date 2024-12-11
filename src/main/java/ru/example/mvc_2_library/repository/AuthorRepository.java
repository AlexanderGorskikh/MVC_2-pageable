package ru.example.mvc_2_library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.mvc_2_library.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}