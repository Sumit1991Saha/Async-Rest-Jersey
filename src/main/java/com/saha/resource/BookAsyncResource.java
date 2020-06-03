package com.saha.resource;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.saha.annotation.PATCH;
import com.saha.dao.BookDao;
import com.saha.exception.NotFoundException;
import com.saha.model.Book;
import org.apache.commons.codec.digest.DigestUtils;
import org.glassfish.jersey.server.ManagedAsync;

import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

@Path("/books-async")
public class BookAsyncResource {

    @Context BookDao bookDao;
    @Context Request request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getBooks(@Suspended AsyncResponse asyncResponse) {
        // bookDao.getBooks();
        ListenableFuture<Collection<Book>> future = bookDao.getBooksAsync();
        Futures.addCallback(future, new FutureCallback<Collection<Book>>() {
            @Override
            public void onSuccess(Collection<Book> books) {
                asyncResponse.resume(books);
            }

            @Override
            public void onFailure(Throwable throwable) {
                asyncResponse.resume(throwable);
            }
        });
    }

    @GET
    @Path("/{book-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getBookById(@PathParam("book-id") long bookId, @Suspended AsyncResponse asyncResponse) {
        // bookDao.getBookById(bookId);
        ListenableFuture<Book> future = bookDao.getBookByIdAsync(bookId);
        Futures.addCallback(future, new FutureCallback<Book>() {
            @Override
            public void onSuccess(Book fetchedBook) {
                EntityTag entityTag = ResourceHelper.createEntityTag(fetchedBook);
                Response response;
                Response.ResponseBuilder rb = request.evaluatePreconditions(entityTag);
                if (rb != null) {
                    asyncResponse.resume(rb.build());
                } else {
                    asyncResponse.resume(Response.ok(fetchedBook).tag(entityTag).build());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                asyncResponse.resume(throwable);
            }
        });
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void createBook(Book book, @Suspended AsyncResponse asyncResponse) {
        //asyncResponse.resume(bookDao.addBook(book));
        ListenableFuture<Book> future = bookDao.addBookAsync(book);
        Futures.addCallback(future, new FutureCallback<Book>() {
            @Override
            public void onSuccess(Book addedBook) {
                asyncResponse.resume(addedBook);
            }

            @Override
            public void onFailure(Throwable throwable) {
                asyncResponse.resume(throwable);
            }
        });
    }

    @PATCH
    @Path("/{book-id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ManagedAsync
    public void updateBook(@PathParam("book-id") long bookId, Book book, @Suspended AsyncResponse asyncResponse)
            throws NotFoundException {

        ListenableFuture<Book> future = bookDao.getBookByIdAsync(bookId);
        Futures.addCallback(future, new FutureCallback<Book>() {
            @Override
            public void onSuccess(Book originalBook) {
                EntityTag entityTag = ResourceHelper.createEntityTag(originalBook);
                Response.ResponseBuilder rb = request.evaluatePreconditions(entityTag);
                if (rb != null) {
                    asyncResponse.resume(rb.build());
                } else {
                    ListenableFuture<Book> future = bookDao.updateBookAsync(bookId, book);
                    Futures.addCallback(future, new FutureCallback<Book>() {
                        @Override
                        public void onSuccess(Book updatedBook) {
                            asyncResponse.resume(updatedBook);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            asyncResponse.resume(throwable);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                asyncResponse.resume(throwable);
            }
        });
    }
}
