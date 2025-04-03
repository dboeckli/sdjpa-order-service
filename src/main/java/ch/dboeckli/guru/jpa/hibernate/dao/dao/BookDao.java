package ch.dboeckli.guru.jpa.hibernate.dao.dao;

import ch.dboeckli.guru.jpa.hibernate.dao.domain.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookDao {
    Book getById(Long id);

    Book findBookByTitle(String title);

    List<Book> findAllBooks();

    List<Book> findAllBooks(int pageSize, int offset);

    List<Book> findAllBooks(Pageable pageable);

    List<Book> findAllBooksSortByTitle(Pageable pageable);

    Book saveNewBook(Book book);

    Book updateBook(Book book);

    void deleteBookById(Long id);
}
