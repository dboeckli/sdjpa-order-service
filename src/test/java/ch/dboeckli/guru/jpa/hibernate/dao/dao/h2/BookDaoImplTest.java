package ch.dboeckli.guru.jpa.hibernate.dao.dao.h2;

import ch.dboeckli.guru.jpa.hibernate.dao.dao.BookDao;
import ch.dboeckli.guru.jpa.hibernate.dao.dao.BookDaoImpl;
import ch.dboeckli.guru.jpa.hibernate.dao.domain.Author;
import ch.dboeckli.guru.jpa.hibernate.dao.domain.Book;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import({ BookDaoImpl.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Slf4j
class BookDaoImplTest {

    @Autowired
    BookDao bookDao;

    @Test
    void testGetById() {
        Book book = bookDao.getById(3L);
        assertThat(book.getId()).isNotNull();
    }

    @Test
    void testFindBookByTitle() {
        Book book = bookDao.findBookByTitle("Clean Code");
        assertThat(book).isNotNull();
    }

    @Test
    void testFindBookByTitleNotFound() {
        assertThrows(EntityNotFoundException.class, () -> bookDao.findBookByTitle("Gugus"));
    }

    @Test
    void testSaveNewBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");

        Author author = new Author();
        author.setId(3L);

        Book saved = bookDao.saveNewBook(book);

        assertThat(saved).isNotNull();
    }

    @Test
    void testUpdateBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");

        Author author = new Author();
        author.setId(3L);

        Book saved = bookDao.saveNewBook(book);

        saved.setTitle("New Book");
        bookDao.updateBook(saved);

        Book fetched = bookDao.getById(saved.getId());

        assertThat(fetched.getTitle()).isEqualTo("New Book");
    }

    @Test
    void testDeleteBookById() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        Book saved = bookDao.saveNewBook(book);

        bookDao.deleteBookById(saved.getId());

        assertThrows(JpaObjectRetrievalFailureException.class, () -> bookDao.getById(saved.getId()));
    }

    @Test
    void findAllBooksPage1_SortByTitle() {
        List<Book> books = bookDao.findAllBooksSortByTitle(PageRequest.of(0, 10,
            Sort.by(Sort.Order.desc("title"))));

        assertAll(
            () -> assertThat(books).isNotNull(),
            () -> assertThat(books).hasSize(10)
        );
    }

    @Test
    void findAllBooksPage1_pageable() {
        List<Book> books = bookDao.findAllBooks(PageRequest.of(0, 10));

        assertAll(
            () -> assertThat(books).isNotNull(),
            () -> assertThat(books).hasSize(10)
        );
    }

    @Test
    void findAllBooksPage2_pageable() {
        List<Book> books = bookDao.findAllBooks(PageRequest.of(1, 10));

        assertAll(
            () -> assertThat(books).isNotNull(),
            () -> assertThat(books).hasSize(10)
        );
    }

    @Test
    void findAllBooksPage10_pageable() {
        List<Book> books = bookDao.findAllBooks(PageRequest.of(10, 10));

        assertAll(
            () -> assertThat(books).isNotNull(),
            () -> assertThat(books).hasSize(0)
        );
    }

    @Test
    void findAllBooksPage1() {
        List<Book> books = bookDao.findAllBooks(10, 0);

        assertAll(
            () -> assertThat(books).isNotNull(),
            () -> assertThat(books).hasSize(10)
        );
    }

    @Test
    void findAllBooksPage2() {
        List<Book> books = bookDao.findAllBooks(10, 10);

        assertAll(
            () -> assertThat(books).isNotNull(),
            () -> assertThat(books).hasSize(10)
        );
    }

    @Test
    void findAllBooksPage10() {
        List<Book> books = bookDao.findAllBooks(10, 100);

        assertAll(
            () -> assertThat(books).isNotNull(),
            () -> assertThat(books).hasSize(0)
        );
    }

    @Test
    void testFindAllBooks() {
        List<Book> books = bookDao.findAllBooks();

        assertAll(
            () -> assertThat(books).isNotNull(),
            () -> assertThat(books).hasSizeGreaterThan(5)
        );
    }
}