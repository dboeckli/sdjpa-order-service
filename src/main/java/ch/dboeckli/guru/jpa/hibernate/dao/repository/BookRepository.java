package ch.dboeckli.guru.jpa.hibernate.dao.repository;

import ch.dboeckli.guru.jpa.hibernate.dao.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBookByTitle(String title);

    Book readByTitle(String title);

    @Nullable
    Book getByTitle(@Nullable String title);

    Stream<Book> findAllByTitleNotNull();

    @Async
    Future<Book> queryByTitle(String title);

    @Query(value = "SELECT b FROM Book b WHERE b.title = ?1")
    Book findBookByTitleWithQuery(String title);

    @Query(value = "SELECT b FROM Book b WHERE b.title = :title")
    Book findBookByTitleWithQueryNamed(@Param("title") String title);

    @Query(value = "SELECT * FROM book WHERE title = :title", nativeQuery = true)
    Book findBookByTitleWithNativeQuery(@Param("title") String title);

    Book jpaNamed(@Param("title") String title);

}
