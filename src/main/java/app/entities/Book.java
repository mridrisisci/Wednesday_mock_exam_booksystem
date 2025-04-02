package app.entities;


import app.dto.BookDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;

    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    @JoinColumn(name = "author_id")
    private Author author;

    private String isbn;

    public Book(BookDTO bookDTO)
    {
        this.id = bookDTO.getId();
        this.title = bookDTO.getTitle();
        this.isbn = bookDTO.getIsbn();
        this.author = bookDTO.getAuthor();
    }

    public Book(Integer id, String title)
    {
        this.id = id;
        this.title = title;
    }

    public Book(String title)
    {
        this.title = title;
    }

}
