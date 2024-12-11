package ru.example.mvc_2_library.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import ru.example.mvc_2_library.model.Book;
import ru.example.mvc_2_library.service.BookService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/admin-ui/books")
@RequiredArgsConstructor
public class BookResource {

    private BookService bookService;

    @GetMapping
    public PagedModel<Book> getAll(Pageable pageable) {
        Page<Book> books = bookService.getAll(pageable);
        return new PagedModel<>(books);
    }

    @GetMapping("/{id}")
    public Book getOne(@PathVariable Long id) {
        return bookService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<Book> getMany(@RequestParam List<Long> ids) {
        return bookService.getMany(ids);
    }

    @PostMapping
    public Book create(@RequestBody Book book) {
        return bookService.create(book);
    }

    @PatchMapping("/{id}")
    public Book patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        return bookService.patch(id, patchNode);
    }

    @DeleteMapping("/{id}")
    public Book delete(@PathVariable Long id) {
        return bookService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        bookService.deleteMany(ids);
    }
}
