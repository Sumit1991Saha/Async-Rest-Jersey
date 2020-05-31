package com.saha.application;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.saha.dao.BookDao;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

public class BookApplication extends ResourceConfig {
    public BookApplication(final BookDao bookDao) {
        JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        packages("com.saha");
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(bookDao).to(BookDao.class);
            }
        });
        register(jacksonJsonProvider);
    }
}
