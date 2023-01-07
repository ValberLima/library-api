package com.cursodsousa.libraryapi.service;

import com.cursodsousa.libraryapi.model.entity.Book;
import com.cursodsousa.libraryapi.exception.BusinessException;
import com.cursodsousa.libraryapi.model.repository.BookRepository;
import com.cursodsousa.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
    BookService bookService;

    @MockBean
    BookRepository bookRepository;
    
    @BeforeEach()
    public void setUp(){
        this.bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        Book book = createValidBook();

        Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(false);

        Mockito.when(bookRepository.save(book)).thenReturn(Book.builder()
                .id(1l)
                .title("As Aventuras")
                .author("Fulano")
                .isbn("123")
                .build());

        Book savedBook = bookService.save(book);

        assertNotNull(savedBook.getId());
        assertEquals("As Aventuras", savedBook.getTitle());
        assertEquals("Fulano", savedBook.getAuthor());
        assertEquals("123", savedBook.getIsbn());
    }

    @Test
    @DisplayName("Deve Lançar erro de negócio ao tentar salvar um livro com isbn duplicado")
    public void naoDeveTerIsbnDuplicado(){
        Book book = createValidBook();
        Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchException(() -> bookService.save(book));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado.");

        Mockito.verify(bookRepository, Mockito.never()).save(book);
    }


    private Book createValidBook() {
        return Book.builder()
                .title("As Aventuras")
                .author("Fulano")
                .isbn("123")
                .build();
    }
}
