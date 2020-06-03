package com.saha.resource;

import com.saha.Constants;
import com.saha.annotation.PATCH;
import com.saha.annotation.PoweredBy;
import com.saha.dao.BookDao;
import com.saha.exception.NotFoundException;
import com.saha.model.Book;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

@Path("/books")
public class BookResource {

    @Context BookDao bookDao;
    @Context Request request;

    @GET
    @Produces({"application/json", "application/xml"})
    public Response getBooks() {
        return Response.ok(bookDao.getBooks()).build();
    }

    @PoweredBy(Constants.BLAH)
    @GET
    @Path("/{book-id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getBookById(@PathParam("book-id") long bookId) throws NotFoundException {
        Book book = bookDao.getBookById(bookId);
        EntityTag entityTag = ResourceHelper.createEntityTag(book);
        Response response;
        Response.ResponseBuilder rb = request.evaluatePreconditions(entityTag);
        if (rb != null) {
            response = rb.build();
        } else {
            response = Response.ok(book).tag(entityTag).build();
        }
        return response;
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createBook(@Valid @NotNull Book book) {
        return Response.ok(bookDao.addBook(book)).build();
    }

    @PATCH
    @Path("/{book-id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateBook(@PathParam("book-id") long bookId, Book book) throws NotFoundException {
        return Response.ok(bookDao.updateBook(bookId, book)).build();
    }
}
