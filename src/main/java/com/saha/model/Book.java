package com.saha.model;

import lombok.Data;

import java.util.Date;

@Data
public class Book {

    long id;
    String title;
    String author;
    String isbn;
    Date publishedDate;
}
