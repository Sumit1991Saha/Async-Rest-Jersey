package com.saha.dao;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.saha.ErrorMessages;
import com.saha.exception.NotFoundException;
import com.saha.model.Book;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class BookDao {

    private long idCounter = 1;
    private Map<Long, Book> books;

    private ListeningExecutorService executorService;

    public BookDao() {
        books = new ConcurrentHashMap<>();
        executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

        Book book1 = new Book();
        book1.setId(idCounter++);
        book1.setAuthor("Author1");
        book1.setTitle("Title1");
        book1.setIsbn("124");
        book1.setPublishedDate(new Date());
        books.put(book1.getId(), book1);

        Book book2 = new Book();
        book2.setId(idCounter++);
        book2.setAuthor("Author2");
        book2.setTitle("Title2");
        book2.setIsbn("12456");
        book2.setPublishedDate(new Date());
        books.put(book2.getId(), book2);
    }

    public Collection<Book> getBooks() {
        return books.values();
    }

    public ListenableFuture<Collection<Book>> getBooksAsync() {
        ListenableFuture<Collection<Book>> future = executorService.submit(new Callable<Collection<Book>>() {
            @Override
            public Collection<Book> call() throws Exception {
                return getBooks();
            }
        });
        return future;
    }

    public Book getBookById(long id) throws NotFoundException {
        Book book = books.get(id);
        if (book == null) {
            throw new NotFoundException(String.format(ErrorMessages.BOOK_NOT_FOUND, id));
        }
        return book;
    }

    public ListenableFuture<Book> getBookByIdAsync(final long id) {
        ListenableFuture<Book> future = executorService.submit(new Callable<Book>() {
            @Override
            public Book call() throws Exception {
                return getBookById(id);
            }
        });
        return future;
    }

    public Book addBook(Book book) {
        long bookId = idCounter++;
        book.setId(bookId);
        books.put(bookId, book);
        return book;
    }

    public ListenableFuture<Book> addBookAsync(final Book book) {
        ListenableFuture<Book> future = executorService.submit(new Callable<Book>() {
            @Override
            public Book call() throws Exception {
                return addBook(book);
            }
        });
        return future;
    }

    public Book updateBook(long id, Book updates) throws NotFoundException {
        if (books.containsKey(id)) {
            Book book = books.get(id);
            if (updates.getAuthor() != null) {
                book.setAuthor(updates.getAuthor());
            }
            if (updates.getTitle() != null) {
                book.setTitle(updates.getTitle());
            }
            if (updates.getIsbn() != null) {
                book.setIsbn(updates.getIsbn());
            }
            if (updates.getPublishedDate() != null) {
                book.setPublishedDate(updates.getPublishedDate());
            }
            if (updates.getExtras() != null) {
                for (String key : updates.getExtras().keySet()) {
                    book.set(key, updates.getExtras().get(key));
                }
            }
            return book;
        } else {
            throw new NotFoundException(String.format(ErrorMessages.BOOK_NOT_FOUND, id));
        }
    }

    public ListenableFuture<Book> updateBookAsync(final long id, final Book book) {
        ListenableFuture<Book> future = executorService.submit(new Callable<Book>() {
            @Override
            public Book call() throws Exception {
                return updateBook(id, book);
            }
        });
        return future;
    }
}
