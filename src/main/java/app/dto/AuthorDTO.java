package app.dto;

import app.entities.Author;
import app.entities.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO
{
    private Integer id;
    private String name;

    private Set<Book> books = new HashSet<>();

    public AuthorDTO(Author author)
    {
        this.name = author.getName();
        this.books = author.getBooks();
    }
}
