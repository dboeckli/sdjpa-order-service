package ch.dboeckli.guru.jpa.hibernate.dao.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

import static ch.dboeckli.guru.jpa.hibernate.dao.domain.Book.JPA_QUERY_NAME;

@Entity
@NoArgsConstructor
@Getter
@Setter
@NamedQuery(name = JPA_QUERY_NAME, query = "FROM Book b where b.title = :title")
public class Book {

    public static final String JPA_QUERY_NAME = "Book.jpaNamed";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String isbn;
    private String publisher;
    private Long authorId;

    public Book(String title, String isbn, String publisher) {
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
