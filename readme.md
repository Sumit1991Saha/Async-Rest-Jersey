Maven command used to setup this project initially:-                
                  
mvn archetype:generate \
    -DgroupId=com.saha \
    -DartifactId=Async-Rest-Jersey \
    -DarchetypeGroupId=org.glassfish.jersey.archetypes \
    -DarchetypeArtifactId=jersey-quickstart-grizzly2 \
    -DarchetypeVersion=2.7 \  
    -Dpackage=com.saha
    
    
    
    
Important Points :-
1. To use the generics with Async request/response use Jackson instead of Moxy due to a bug in Moxy layer.
   Following is the exception if done so :-
      org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException:
      MessageBodyWriter not found for media type=application/json,
      type=class java.util.concurrent.ConcurrentHashMap$ValuesView,
      genericType=class java.util.concurrent.ConcurrentHashMap$ValuesView.
2. @JsonInclude(JsonInclude.Include.NON_NULL) :- 
    This property tells jackson to not include null values while deserializing the response.
3. @JsonIgnoreProperties(ignoreUnknown = true) :- 
    This property tells jackson to ignore unknown properties if provides while serializing the request object 
    to create the corresponding model object.
4. jackson-databind library, part of jackson-core framework, helps to provide custom Serializer and Deserializer. 
    And also this library helps to convert Hasmap objects into model objects during post request. (https://www.baeldung.com/jackson-map)
5. using @JsonAnyGetter and @JsonAnySetter on a model object, 
   we have given jackson a data structure to store and retrieve arbitrary values for a model object.