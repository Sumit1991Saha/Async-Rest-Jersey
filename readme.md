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
2.  