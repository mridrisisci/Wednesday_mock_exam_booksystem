package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.controllers.BookController;
import app.controllers.SecurityController;
import app.routes.Routes;
import jakarta.persistence.EntityManagerFactory;


public class Main
{
    private final static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();


    public static void main(String[] args)
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
                .startServer(7070);
    }
}