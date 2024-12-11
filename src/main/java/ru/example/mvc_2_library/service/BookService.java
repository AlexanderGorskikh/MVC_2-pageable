package ru.example.mvc_2_library.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.example.mvc_2_library.model.Book;
import ru.example.mvc_2_library.repository.BookRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
public class BookService {

    private final BookRepository bookRepository;

    private final ObjectMapper objectMapper;

    public BookService(BookRepository bookRepository, ObjectMapper objectMapper) {
        this.bookRepository = bookRepository;
        this.objectMapper = objectMapper;
    }

    public Page<Book> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book getOne(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        return bookOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public List<Book> getMany(List<Long> ids) {
        return bookRepository.findAllById(ids);
    }

    public Book create(Book book) {
        return bookRepository.save(book);
    }

    public Book patch(Long id, JsonNode patchNode) throws IOException {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
        objectMapper.readerForUpdating(book).readValue(patchNode);
        return bookRepository.save(book);
    }

    public Book delete(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            bookRepository.delete(book);
        }
        return book;
    }

    public void deleteMany(List<Long> ids) {
        bookRepository.deleteAllById(ids);
    }
}
