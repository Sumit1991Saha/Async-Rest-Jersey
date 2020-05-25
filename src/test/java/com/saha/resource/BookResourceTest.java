package com.saha.resource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import com.saha.application.BookApplication;
import com.saha.dao.BookDao;
import com.saha.model.Book;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import java.util.Collection;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;

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
        Book book = target("books").path("1").request().get(Book.class);
        assertNotNull(book);
    }

    @Test
    public void getBooks() {
        Collection<Book> books = target("books").request().get(new GenericType<Collection<Book>>() {});
        assertEquals(2, books.size());
    }

    @Test
    public void testDao() {
        //Each call to this api results in instantiation of Dao Class again and again
        //hence the objects are different. This problem can be solved by dependency injection.
        Book book1 = target("books").path("1").request().get(Book.class);
        Book book2 = target("books").path("1").request().get(Book.class);
        assertEquals(book1, book2);
    }
}
