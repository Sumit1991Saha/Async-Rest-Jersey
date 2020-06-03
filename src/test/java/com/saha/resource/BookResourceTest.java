package com.saha.resource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.saha.ErrorMessages;
import com.saha.application.BookApplication;
import com.saha.dao.BookDao;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BookResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        BookDao bookDao = new BookDao();
        return new BookApplication(bookDao);
    }

    protected void configureClient(ClientConfig clientConfig) {
        JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJsonProvider.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true);
        clientConfig.register(jacksonJsonProvider);
    }

    private Response addBook(String resourcePath, String author, String title, String isbn, Date date, String... extras) {
        Map<String, Object> bookMap = new HashMap<String, Object>() {{
            put("author", author);
            put("title", title);
            put("isbn", isbn);
            put("publishedDate", date);
            if (extras != null) {
                int count = 1;
                for (String extra : extras) {
                    put("extra" + count++, extra);
                }
            }
        }};
        Entity<Map<String, Object>> entity = Entity.entity(bookMap, MediaType.APPLICATION_JSON);
        return target(resourcePath).request().post(entity);
    }

    private HashMap<String, Object> readEntityToHashMap(Response response) {
        return response.readEntity(new GenericType<HashMap<String, Object>>(){});
    }

    @Test
    public void addBook() {
        Response response = addBook("/books", "Author1", "Title1", "1234", new Date());

        assertEquals(200, response.getStatus());
        HashMap<String, Object> createdBook = readEntityToHashMap(response);
        assertNotNull(createdBook.get("id"));
        assertEquals("Author1", createdBook.get("author"));
    }

    @Test
    public void addBookAsync() {
        Response response = addBook("/books-async", "Author2", "Title2", "12345", new Date());

        assertEquals(200, response.getStatus());
        HashMap<String, Object> createdBook = readEntityToHashMap(response);
        assertNotNull(createdBook.get("id"));
        assertEquals("Author2", createdBook.get("author"));
    }

    @Test
    public void getBook() {
        Response response = target("books").path("1").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        HashMap<String, Object> book = readEntityToHashMap(response);
        assertNotNull(book);
    }

    @Test
    public void getBookAsync() {
        Response response = target("books-async").path("1").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        HashMap<String, Object> book = readEntityToHashMap(response);
        assertNotNull(book);
    }

    @Test
    public void getBooks() {
        Response response = target("books").request(MediaType.APPLICATION_JSON).get();
        //there is some issue with XML type due to a class cast excpetion in custom Message body writer.
        //Response response = target("books").request(MediaType.APPLICATION_XML).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Collection<HashMap<String, Object>> books =
                response.readEntity(new GenericType<Collection<HashMap<String, Object>>>() {});
        assertEquals(2, books.size());
    }

    @Test
    public void getBooksAsync() {
        // This call doesn't work with Moxy - JSON (currently a bug exist) in returning a response which has Collections.
        // this leads to
        // org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException:
        // MessageBodyWriter not found for media type=application/json,
        // type=class java.util.concurrent.ConcurrentHashMap$ValuesView,
        // genericType=class java.util.concurrent.ConcurrentHashMap$ValuesView.

        // To use the generics with Async request/response use Jackson.
        Response response = target("books-async").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Collection<HashMap<String, Object>> books =
                response.readEntity(new GenericType<Collection<HashMap<String, Object>>>() {});
        assertEquals(2, books.size());
    }

    @Test
    public void testDao() {
        //Each call to this api results in instantiation of Dao Class again and again
        //hence the objects are different. This problem can be solved by dependency injection.
        Response response1 = target("books").path("1").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response1.getStatus());
        HashMap<String, Object> book1 = readEntityToHashMap(response1);

        Response response2 = target("books").path("1").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response2.getStatus());
        HashMap<String, Object> book2 = readEntityToHashMap(response2);

        assertEquals(book1, book2);
    }

    @Test
    public void addBookWithExtraProperty() {
        String newProperty = "some random property";
        Response response = addBook("/books", "Author1", "Title", "1234", new Date(),
                newProperty);

        assertEquals(200, response.getStatus());
        HashMap<String, Object> createdBook = response.readEntity(new GenericType<HashMap<String, Object>>() {});
        assertNotNull(createdBook.get("id"));
        assertEquals(createdBook.get("extra1"), newProperty);
    }

    @Test
    public void addBookWithInvalidAuthor() {
        Response response = addBook("/books", null, "Title", "1234", new Date());

        assertEquals(400, response.getStatus());
        String message = response.readEntity(String.class);
        assertTrue(message.contains(ErrorMessages.INVALID_AUTHOR));
    }

    @Test
    public void addBookWithInvalidTitle() {
        Response response = addBook("/books", "Author1", null, "1234", new Date());

        assertEquals(400, response.getStatus());
        String message = response.readEntity(String.class);
        assertTrue(message.contains(ErrorMessages.INVALID_TITLE));
    }

    @Test
    public void addBookWithInvalidBook() {
        Response response = target("/books").request().post(null);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void getInvalidBook() {
        final String bookId = "100";
        Response response = target("books").path(bookId).request().get();

        assertEquals(404, response.getStatus());
        assertEquals(String.format(ErrorMessages.BOOK_NOT_FOUND, bookId) , response.readEntity(String.class));
    }

    @Test
    public void entityTagNotModified() {
        EntityTag entityTag = target("books").path("1").request(MediaType.APPLICATION_JSON).get().getEntityTag();
        assertNotNull(entityTag);

        Response response = target("books").path("1").request().header("If-None-Match", entityTag).get();
        assertEquals(304, response.getStatus());
    }


}
