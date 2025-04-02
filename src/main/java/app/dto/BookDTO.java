package app.dto;


import app.entities.Author;
import app.entities.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO
{
    private Integer id;
    private String title;

    private Author author;

    private String isbn;

    public BookDTO(Book book)
    {
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
        this.author = book.getAuthor();
    }


}
