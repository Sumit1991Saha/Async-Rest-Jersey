package com.saha.resource;

import com.saha.dao.BookDao;
import com.saha.model.Book;

import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/books")
public class BookResource {

    @Context BookDao bookDao;

    @GET
    @Produces({"application/json", "application/xml"})
    public Collection<Book> getBooks() {
        return bookDao.getBooks();
    }

    @GET
    @Path("/{book-id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Book getBookById(@PathParam("book-id") long bookId) {
        return bookDao.getBookById(bookId);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Book createBook(Book book) {
        return bookDao.addBook(book);
    }
}
