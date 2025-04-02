package app.controllers;

import app.dao.AuthorDAO;
import app.dao.CrudDAO;
import app.dto.AuthorDTO;
import app.dto.BookDTO;
import app.dto.ErrorMessage;
import app.entities.Author;
import app.entities.Book;
import app.utils.Populator;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BookController implements IController
{
    private final CrudDAO dao;
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);


    public BookController(EntityManagerFactory emf)
    {
        dao = new AuthorDAO(emf);
    }

    public BookController(CrudDAO dao)
    {
        this.dao = dao;
    }

    public void searchBookByAuthor(Context ctx)
    {
        try
        {
            String param = ctx.pathParam("author");
            Author author = dao.getById(Author.class, Long.parseLong(param));
            if (author == null)
            {
                ctx.status(404).json(new ErrorMessage("No author with that id"));
                return;
            }
            ctx.json(author.getBooks());
        }
        catch (Exception ex)
        {
            logger.error("Error getting books by author", ex);
            ErrorMessage error = new ErrorMessage("Error getting books by author");
            ctx.status(404).json(error);
        }
    }

    public void populateDB(EntityManagerFactory emf)
    {
        Populator populator = new Populator();
        try (EntityManager em = emf.createEntityManager())
        {
            populator.resetAndPersistEntities(em);
            logger.info("Populated database with dummy data");
        } catch (Exception e)
        {
            logger.error("Error populating database: " + e.getMessage());
        }

    }


    @Override
    public void getAll(Context ctx)
    {
        try
        {
            ctx.json(dao.getAll(Book.class));
        }
        catch (Exception ex)
        {
            logger.error("Error getting entities", ex);
            ErrorMessage error = new ErrorMessage("Error getting entities");
            ctx.status(404).json(error);
        }
    }

    @Override
    public void getById(Context ctx)
    {

        try {
            //long id = Long.parseLong(ctx.pathParam("id"));
            long id = ctx.pathParamAsClass("id", Long.class)
                    .check(i -> i>0, "id must be at least 0")
                    .getOrThrow((valiappor) -> new BadRequestResponse("Invalid id"));
            BookDTO foundEntity = new BookDTO(dao.getById(Book.class, id));
            ctx.json(foundEntity);

        } catch (Exception ex){
            logger.error("Error getting entity", ex);
            ErrorMessage error = new ErrorMessage("No entity with that id");
            ctx.status(404).json(error);
        }
    }

    @Override
    public void create(Context ctx)
    {
        try
        {
            BookDTO incomingTest = ctx.bodyAsClass(BookDTO.class);
            Book entity = new Book(incomingTest);
            Book createdEntity = dao.create(entity);
            /*
            * Use 'merge' method when getting the error 'detached entity passed to persist'
            * */
            Book createdEntity1 = dao.create(entity);
            ctx.json(new BookDTO(createdEntity));
        }
        catch (Exception ex)
        {
            logger.error("Error creating entity", ex);
            ErrorMessage error = new ErrorMessage("Error creating entity");
            ctx.status(400).json(error);
        }
    }

    public void update(Context ctx)
    {
        try
        {
            //int id = Integer.parseInt(ctx.pathParam("id"));
            long id = ctx.pathParamAsClass("id", Long.class)
                .check(i -> i>0, "id must be at least 0")
                .getOrThrow((valiappor) -> new BadRequestResponse("Invalid id"));
            BookDTO incomingEntity = ctx.bodyAsClass(BookDTO.class);
            Book authorToUpdate = dao.getById(Book.class, id);
            if (incomingEntity.getTitle() != null)
            {
                authorToUpdate.setTitle(incomingEntity.getTitle());
            }
            /*if (incomingEntity.getName() != null)
            {
                authorToUpdate.setName(incomingEntity.());
            }*/
            Book updatedEntity = dao.update(authorToUpdate);
            BookDTO returnedEntity = new BookDTO(updatedEntity);
            ctx.json(returnedEntity);
        }
        catch (Exception ex)
        {
            logger.error("Error upapping entity", ex);
            ErrorMessage error = new ErrorMessage("Error upapping entity. " + ex.getMessage());
            ctx.status(400).json(error);
        }
    }



    public void delete(Context ctx)
    {
        try
        {
            //long id = Long.parseLong(ctx.pathParam("id"));
            long id = ctx.pathParamAsClass("id", Long.class)
                    .check(i -> i>0, "id must be at least 0")
                    .getOrThrow((valiappor) -> new BadRequestResponse("Invalid id"));
            dao.delete(Book.class, id);
            ctx.status(204);
        }
        catch (Exception ex)
        {
            logger.error("Error deleting entity", ex);
            ErrorMessage error = new ErrorMessage("Error deleting entity");
            ctx.status(400).json(error);
        }
    }

    public void getRooms(@NotNull Context context)
    {
        try
        {
            long id = context.pathParamAsClass("id", Long.class)
                    .check(i -> i>0, "id must be at least 0")
                    .getOrThrow((valiappor) -> new BadRequestResponse("Invalid id"));
            Author author = dao.getById(Author.class, id);
            context.json(author.getBooks());
        }
        catch (Exception ex)
        {
            logger.error("Error getting rooms", ex);
            ErrorMessage error = new ErrorMessage("Error getting rooms");
            context.status(404).json(error);
        }
    }
}
