package app.dao;

import app.entities.Author;
import app.entities.Book;
import app.exceptions.DaoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Set;

public class AuthorDAO extends GenericDAO implements IAuthorDAO
{
    public AuthorDAO(EntityManagerFactory emf)
    {
        super(emf);
    }

    public List<Author> getAllAuthors()
    {
        return super.getAll(Author.class);
    }

    public Author getAuthorById(Long id)
    {
        return super.getById(Author.class, id);
    }

    public Author createAuthor(Author author)
    {
        return super.create(author);
    }

    public Author updateAuthor(Author hotel)
    {
        return super.update(hotel);
    }

    public void deleteAuthor(Long id)
    {
        super.delete(Author.class, id);
    }

    public <T> T merge(T object)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.merge(object);
            em.getTransaction().commit();
            return object;
        }
        catch (Exception e)
        {
            //logger.error("Error persisting object to db", e);
            throw new DaoException("Error persisting object to db. ", e);
        }
    }

    @Override
    public Author addBook(Author author, Book room)
    {
        author.addBook(room);
        return update(author);
    }

    @Override
    public Author deleteBook(Author author, Book book)
    {
        author.deleteBook(book);
        Author updatedAuthor = update(author);
        delete(book);
        return updatedAuthor;
    }

    @Override
    public Set<Book> getBooksForAuthor(Author author)
    {
        return author.getBooks();
    }
}
