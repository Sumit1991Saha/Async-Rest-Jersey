package com.saha.resource;

import com.saha.dao.BookDao;
import com.saha.model.Book;
import org.apache.commons.codec.digest.DigestUtils;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;

public class ResourceHelper {

    @Context
    BookDao bookDao;
    @Context
    Request request;

    static EntityTag createEntityTag(Book book) {
        return new EntityTag(DigestUtils.md5Hex(book.toString()));
    }
}
