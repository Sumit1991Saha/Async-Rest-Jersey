This Project uses Grizzly as web server to host the application.
Grizzly is a pure Java web service built using the NIO API. 
Grizzly's main use case is the web server component for the GlassFish application server.

To run this project :-
1. `mvn clean install`
2. `java -jar target/async-rest-jersey-sample-1.0-SNAPSHOT.jar`
3. Go to `http://localhost:8080/async-rest-jersey/books` it should show some hardcoded data

To build the docker image :-
`docker build -t async-rest-jersey -f Dockerfile .`

To run the app in the container :-
`docker run -d -p 8080:8080 async-rest-jersey`

TODO :- localhost is inaccessible inside docker, need to see how to create proper uri when passed to Grizzly server

Maven command used to setup this project initially:-                
                  
mvn archetype:generate \
    -DgroupId=com.saha \
    -DartifactId=Async-Rest-Jersey \
    -DarchetypeGroupId=org.glassfish.jersey.archetypes \
    -DarchetypeArtifactId=jersey-quickstart-grizzly2 \
    -DarchetypeVersion=2.7 \  
    -Dpackage=com.saha
    
Add this in main method, to add logging capability to grizzly server :- 
Logger l = Logger.getLogger("org.glassfish.grizzly.http.server.HttpHandler");
l.setLevel(Level.FINE);
l.setUseParentHandlers(false);
ConsoleHandler ch = new ConsoleHandler();
ch.setLevel(Level.ALL);
l.addHandler(ch);
    
    
    
    
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
6. By removing the dependency of server side code (ie model object) on client side by using hashmap instead of model object,
    client side doesn't need to know the current status of model object on the server side code, 
    ie. if at all any attribute is removed from model object, our extra's hashmap coupled with @JsonAnyGetter and @JsonAnySetter
    will cater to that field sent by the client. 
    If client depends on the model object then any change on the server side model object (especially removing attribute) would have broken the client. 
    So instead of using Model objects, using hashmap on client side has made the client backward/forward compatible.
7. Using grizzly connector (org.glassfish.jersey.connectors:jersey-grizzly-connector) to support Patch HTTP Method, 
   or use HttpMethodOverrideFilter to use the PATCH method using post technique (in this case default connector provided by Jersey will work).
8. By default Custom-Request-Filters are Post matching filter ie it is invoked after resource method is selected, 
   to make it pre matching filter it needs to be annotated with @PreMatching
9. Using @NameBinding acts as a great tool to handle specific resources in our custom filter.