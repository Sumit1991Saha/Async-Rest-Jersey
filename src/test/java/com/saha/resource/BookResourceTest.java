package com.saha.resource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import com.saha.model.Book;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import java.util.Collection;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;

public class BookResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig().packages("com.saha");
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
}
