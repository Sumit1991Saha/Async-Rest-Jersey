package com.saha.resource;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.saha.dao.BookDao;
import com.saha.model.Book;
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
import javax.ws.rs.core.MediaType;

@Path("/books-async")
public class BookAsyncResource {

    @Context BookDao bookDao;

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
            public void onSuccess(Book addedBook) {
                asyncResponse.resume(addedBook);
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
}
