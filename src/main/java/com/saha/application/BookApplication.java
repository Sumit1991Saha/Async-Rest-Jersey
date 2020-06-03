package com.saha.application;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider;
import com.saha.dao.BookDao;
import com.saha.mapper.NotFoundExceptionMapper;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

public class BookApplication extends ResourceConfig {
    public BookApplication(final BookDao bookDao) {
        JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true);
        JacksonXMLProvider jacksonXMLProvider = new JacksonXMLProvider()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true);
        packages("com.saha");
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(bookDao).to(BookDao.class);
            }
        });
        register(jacksonJsonProvider);
        register(jacksonXMLProvider);
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        //Either add these with @Provider annotation or register in the Resource config class.
        //register(NotFoundExceptionMapper.class);
    }
}
