package ru.example.mvc_2_library;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.example.mvc_2_library.model.Book;
import ru.example.mvc_2_library.repository.BookRepository;
import ru.example.mvc_2_library.service.BookService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(Collections.singletonList(new Book()));

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<Book> result = bookService.getAll(pageable);

        assertEquals(bookPage, result);
        verify(bookRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testGetOneFound() {
        Book book = new Book();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getOne(1L);

        assertEquals(book, result);
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetOneNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> bookService.getOne(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetMany() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<Book> books = Arrays.asList(new Book(), new Book());

        when(bookRepository.findAllById(ids)).thenReturn(books);

        List<Book> result = bookService.getMany(ids);

        assertEquals(books, result);
        verify(bookRepository, times(1)).findAllById(ids);
    }

    @Test
    public void testCreate() {
        Book book = new Book();
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.create(book);

        assertEquals(book, result);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testPatch() throws IOException {
        Book book = new Book();
        JsonNode patchNode = mock(JsonNode.class);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        ObjectReader mockReader = mock(ObjectReader.class);
        when(objectMapper.readerForUpdating(book)).thenReturn(mockReader);
        when(mockReader.readValue(patchNode)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.patch(1L, patchNode);

        assertEquals(book, result);
        verify(bookRepository, times(1)).findById(1L);
        verify(objectMapper, times(1)).readerForUpdating(book);
        verify(mockReader, times(1)).readValue(patchNode);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testDeleteFound() {
        Book book = new Book();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.delete(1L);

        assertEquals(book, result);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    public void testDeleteNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Book result = bookService.delete(1L);

        assertNull(result);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, never()).delete(any());
    }

    @Test
    public void testDeleteMany() {
        List<Long> ids = Arrays.asList(1L, 2L);

        doNothing().when(bookRepository).deleteAllById(ids);

        bookService.deleteMany(ids);

        verify(bookRepository, times(1)).deleteAllById(ids);
    }
}

