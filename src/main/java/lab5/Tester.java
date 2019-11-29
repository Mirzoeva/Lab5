package lab5;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import org.asynchttpclient.AsyncHttpClient;

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


}
