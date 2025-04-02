package app.entities;

import app.dto.AuthorDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "authors")
public class Author
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @ToString.Exclude
    @JsonManagedReference
    private Set<Book> books = new HashSet<>();

    public Author(AuthorDTO authorDTO)
    {
        this.id = authorDTO.getId();
        this.name = authorDTO.getName();
        this.books = authorDTO.getBooks();
    }

    public Author(String name)
    {
        this.name = name;
    }

    public void addBook(Book book)
    {
        books.add(book);
        book.setAuthor(this);
    }

    public void deleteBook(Book book)
    {
        books.remove(book);
        book.setAuthor(null);
    }
}
