package com.saha.resource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import com.saha.application.BookApplication;
import com.saha.dao.BookDao;
import com.saha.model.Book;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import java.util.Collection;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public class BookResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        BookDao bookDao = new BookDao();
        return new BookApplication(bookDao);
    }

    @Test
    public void getBook() {
        Response response = target("books").path("1").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Book book = response.readEntity(Book.class);
        assertNotNull(book);
    }

    @Test
    public void getBooks() {
        Response response = target("books").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Collection<Book> books = response.readEntity(new GenericType<Collection<Book>>() {});
        assertEquals(2, books.size());
    }

    @Test
    public void testDao() {
        //Each call to this api results in instantiation of Dao Class again and again
        //hence the objects are different. This problem can be solved by dependency injection.
        Response response1 = target("books").path("1").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response1.getStatus());
        Book book1 = response1.readEntity(Book.class);

        Response response2 = target("books").path("1").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response2.getStatus());
        Book book2 = response2.readEntity(Book.class);

        assertEquals(book1, book2);
    }
}
