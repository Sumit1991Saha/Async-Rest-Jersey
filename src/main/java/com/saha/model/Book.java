package com.saha.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    long id;
    String title;
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
