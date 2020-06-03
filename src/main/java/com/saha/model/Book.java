package com.saha.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.saha.ErrorMessages;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;

@Data
@JsonPropertyOrder({"id"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "book")  //to change the XML root tag name
public class Book {

    @JacksonXmlProperty(isAttribute = true)
    long id;
    @NotNull(message = ErrorMessages.INVALID_TITLE)
    String title;
    @NotNull(message = ErrorMessages.INVALID_AUTHOR)
    String author;
    String isbn;
    Date publishedDate;

    Map<String, Object> extras = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getExtras() {
        return this.extras;
    }

    @JsonAnySetter
    public void setExtras(String key, Object value) {
        this.extras.put(key, value);
    }

}
