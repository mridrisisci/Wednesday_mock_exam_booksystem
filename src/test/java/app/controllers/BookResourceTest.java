package app.controllers;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.dto.AuthorDTO;
import app.dto.BookDTO;
import app.entities.Author;
import app.entities.Book;
import app.routes.Routes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookResourceTest
{

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    final ObjectMapper objectMapper = new ObjectMapper();
    Author a1, a2;
    Book b1, b2, b3;
    final Logger logger = LoggerFactory.getLogger(BookResourceTest.class.getName());


    @BeforeAll
    static void setUpAll()
    {
        BookController bookController = new BookController(emf);
        SecurityController securityController = new SecurityController(emf);
        Routes routes = new Routes(bookController, securityController);
        ApplicationConfig
                .getInstance()
                .initiateServer()
                .setRoute(routes.getRoutes())
                .handleException()
                .setApiExceptionHandling()
                .checkSecurityRoles()
                .startServer(7078);
        RestAssured.baseURI = "http://localhost:7078/api";
    }

    @BeforeEach
    void setUp()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            //TestEntity[] entities = EntityPopulator.populate(genericDAO);
            a1 = new Author("Test Author A");
            a2 = new Author("Test Author B");
            b1 = new Book( "Book1");
            b2 = new Book( "Book2");
            b3 = new Book( "Book3");
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Author ").executeUpdate();
            em.createQuery("DELETE FROM Book ").executeUpdate();

            em.persist(a1);
            em.persist(a2);

            em.persist(b1);
            em.persist(b2);
            em.persist(b3);
            em.getTransaction().commit();
        }
        catch (Exception e)
        {
            logger.error("Error setting up test", e);
        }
    }

    @Test
    void getAll()
    {
        given().when().get("/books/all").then().statusCode(200).body("size()", equalTo(3));
    }

    @Test
    void getById()
    {
        given().when().get("/books/" + b2.getId()).then().statusCode(200).body("id", equalTo(b2.getId().intValue()));
    }

    @Test
    void create()
    {
        Author entity = new Author("HC Andersen");
        Book book = new Book("Peter Plys");
        entity.addBook(book);
        try
        {
            String json = objectMapper.writeValueAsString(new BookDTO(book));
            given().when()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(json)
                    .post("/create")
                    .then()
                    .statusCode(200)
                    .body("title", equalTo(book.getTitle()));
                    //.body("address", equalTo(entity.getAddress()));
                    //.body("rooms.size()", equalTo(1));
        } catch (JsonProcessingException e)
        {
            logger.error("Error creating hotel", e);

            fail();
        }
    }

    @Test
    void update()
    {
        Author entity = new Author("New entity2");
        try
        {
            String json = objectMapper.writeValueAsString(new AuthorDTO(entity));
            given().when()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(json)
                    .put("/books/" + a1.getId()) // double check id
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("New entity2"));
        } catch (JsonProcessingException e)
        {
            logger.error("Error updating entity", e);
            fail();
        }
    }

    @Test
    void delete()
    {
        given().when()
                .delete("/books/" + a1.getId())
                .then()
                .statusCode(204);
    }
}