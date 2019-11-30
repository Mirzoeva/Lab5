package lab5;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.asynchttpclient.AsyncHttpClient;

import javax.management.Query;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class Tester {
    private final ActorMaterializer materializer;
    private final ActorRef storage;
    private final AsyncHttpClient asyncHttpClient;
    private final int countOfRequests;

    public Tester(AsyncHttpClient asyncHttpClient, ActorSystem system, ActorMaterializer materializer){
        this.asyncHttpClient = asyncHttpClient;
        this.materializer = materializer;
        this.storage = system.actorOf();
        this.countOfRequests = Constants.countOfRequests;
    }

    public Flow<HttpRequest, HttpResponse, NotUsed> createRoute(){
        return Flow.of(HttpRequest.class)
                .map(this::parseRequest)
                .mapAsync(countOfRequests,)
                .map();
    }

    public UrlTest parseRequest(HttpRequest request){
        Query query = request.getUri().query();
        Optional<String> testUrl = query.get("testUrl");
        Optional<String> count = query.get("count");
        return new UrlTest(testUrl.get(), Integer.parseInt(count.get()));
    }

    

}
