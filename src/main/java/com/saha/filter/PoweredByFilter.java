package com.saha.filter;

import com.saha.Constants;
import com.saha.annotation.PoweredBy;

import java.io.IOException;
import java.lang.annotation.Annotation;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class PoweredByFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext,
                       ContainerResponseContext containerResponseContext) throws IOException {
        for(Annotation annotation : containerResponseContext.getEntityAnnotations()) {
            if (annotation.annotationType() == PoweredBy.class) {
                String value = ((PoweredBy)annotation).value();
                containerResponseContext.getHeaders().add(Constants.X_POWERED_BY, value);
            }
        }
    }
}
