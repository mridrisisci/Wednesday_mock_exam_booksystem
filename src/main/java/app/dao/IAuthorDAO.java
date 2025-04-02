package app.dao;

import app.entities.Author;
import app.entities.Book;

import java.util.List;
import java.util.Set;

public interface IAuthorDAO
{
    Author getAuthorById(Long id);
    Author addBook(Author author, Book book);
    Author deleteBook(Author author, Book book);
    Set<Book> getBooksForAuthor(Author author);
}
