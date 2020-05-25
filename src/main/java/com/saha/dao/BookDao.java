package com.saha.dao;

import com.saha.model.Book;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookDao {

    private long idCounter = 1;
    private Map<Long, Book> books;

    public BookDao() {
        books = new HashMap<>();

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

    public Book getBookById(long id) {
        return books.get(id);
    }

    public Book addBook(Book book) {
        long bookId = idCounter++;
        book.setId(bookId);
        books.put(bookId, book);
        return book;
    }
}
