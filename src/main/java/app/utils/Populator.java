package app.utils;


import app.entities.Author;
import app.entities.Book;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Populator
{

    // declare instance variables here
    Book book1, book2, book3, book4, book5, book6;
    Author author1, author2;

    public Populator()
    {
        // initialize dummy objects here
        book1 = Book.builder()
            .title("Harry Potter and the Philosopher's Stone")
            .isbn("978-0747532699")
            .build();
        book2 = Book.builder()
            .title("Harry Potter and the Chamber of Secrets")
            .isbn("978-0747538493")
            .build();
        book3 = Book.builder()
            .title("Harry Potter and the Prisoner of Azkaban")
            .isbn("978-0747542155")
            .build();
        book4 = Book.builder()
            .title("Harry Potter and the Goblet of Fire")
            .isbn("978-0747546245")
            .build();
        book5 = Book.builder()
            .title("Harry Potter and the Order of the Phoenix")
            .isbn("978-0747551003")
            .build();
        book6 = Book.builder()
            .title("Harry Potter and the Half-Blood Prince")
            .isbn("978-0747581085")
            .build();
        author1 = Author.builder()
            .name("J.K. Rowling")
            .build();
        author2 = Author.builder()
            .name("J.R.R. Tolkien")
            .build();
    }

    public Map<String, Author> getAuthors()
    {
        Map<String, Author> authors = new HashMap<>();
        authors.put("author1", author1);
        authors.put("author2", author2);
        return authors;
    }
    public Map<String, Book> getBooks()
    {
        Map<String, Book> books = new HashMap<>();
        books.put("book1", book1);
        books.put("book2", book2);
        books.put("book3", book3);
        books.put("book4", book4);
        books.put("book5", book5);
        books.put("book6", book6);
        return books;
    }

    public void resetAndPersistEntities(EntityManager em)
    {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Book").executeUpdate();
        em.createQuery("DELETE FROM Author").executeUpdate();
        for (Author entity : getAuthors().values())
        {
            em.persist(entity);
        }
        for (Book entity : getBooks().values())
        {
            em.persist(entity);
        }
        // ensures bi-directional mapping
        author1.addBook(book1);
        author1.addBook(book2);
        author1.addBook(book3);

        author2.addBook(book4);
        author2.addBook(book5);
        author2.addBook(book6);

        // ensures bi-directional mapping is updated to DB
        author1 = em.merge(author1);
        author2 = em.merge(author2);
        em.getTransaction().commit();
    }
}
