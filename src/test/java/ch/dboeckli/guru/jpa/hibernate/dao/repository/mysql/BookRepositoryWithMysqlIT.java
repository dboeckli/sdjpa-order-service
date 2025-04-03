package ch.dboeckli.guru.jpa.hibernate.dao.repository.mysql;

import ch.dboeckli.guru.jpa.hibernate.dao.domain.Book;
import ch.dboeckli.guru.jpa.hibernate.dao.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test_mysql")
@DirtiesContext
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // to assure that it is not replaced with h2
@Slf4j
class BookRepositoryWithMysqlIT {

    @Autowired
    BookRepository bookRepository;

    @Test
    void testEmptyResultException() {
        assertThrows(EmptyResultDataAccessException.class, () -> bookRepository.readByTitle("foobar4"));
    }

    @Test
    void testNullParam() {
        assertNull(bookRepository.getByTitle(null));
    }

    @Test
    void testNoException() {
        assertNull(bookRepository.getByTitle("foo"));
    }

    @Test
    void testStreamFindAllByTitleNotNull() {
        AtomicInteger count = new AtomicInteger();
        bookRepository.findAllByTitleNotNull().forEach(book -> {
            log.info("Found book: {}", book);
            count.incrementAndGet();
        });
        assertThat(count.get()).isGreaterThan(0);
    }

    @Test
    void testQueryByTitleAsync() throws ExecutionException, InterruptedException {
        Future<Book> booksFuture = bookRepository.queryByTitle("Clean Code");
        Book book = booksFuture.get();

        assertNotNull(book);
    }

    @Test
    void testFindBookByTitleWithQuery() {
        Book book = bookRepository.findBookByTitleWithQuery("Clean Code");

        assertNotNull(book);
        assertEquals("Clean Code", book.getTitle());
    }

    @Test
    void testFindBookByTitleWithQueryNamed() {
        Book book = bookRepository.findBookByTitleWithQueryNamed("Clean Code");

        assertNotNull(book);
        assertEquals("Clean Code", book.getTitle());
    }

    @Test
    void testFindBookByTitleWithQueryNative() {
        Book book = bookRepository.findBookByTitleWithNativeQuery("Clean Code");

        assertNotNull(book);
        assertEquals("Clean Code", book.getTitle());
    }

    @Test
    void testJpaNamedTestQuery() {
        Book book = bookRepository.jpaNamed("Clean Code");

        assertNotNull(book);
        assertEquals("Clean Code", book.getTitle());
    }

    @Test
    void testJpaTestSplice() {
        long countBefore = bookRepository.count();

        bookRepository.save(new Book("My Book", "1235555", "Self"));

        long countAfter = bookRepository.count();

        assertThat(countAfter).isEqualTo(countBefore + 1);
    }

}