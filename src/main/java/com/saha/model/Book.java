package com.saha.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {
    long id;
    String title;
    String author;
    String isbn;
    Date publishedDate;
}
