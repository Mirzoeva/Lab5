package lab5;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.ContentType;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.StatusCode;
import akka.http.javadsl.model.StatusCodes;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.util.ByteString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.AsyncHttpClient;

import javax.management.Query;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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
                .mapAsync(countOfRequests, test -> {
                    return Patterns.ask(storage, test, Duration.ofSeconds(5))
                            .thenApply(o -> (ReturnUrlTestResult)o)
                            .thenCompose(result -> {
                                Optional<TestResult> resOpt = result.get();
                                return resOpt.isPresent() ? CompletableFuture.completedFuture(resOpt.get())
                                        : runTest(test);
                            });
                })
                .map(this::saveAndCreateResponse);
    }

    public UrlTest parseRequest(HttpRequest request){
        Query query = request.getUri().query();
        Optional<String> testUrl = query.get("testUrl");
        Optional<String> count = query.get("count");
        return new UrlTest(testUrl.get(), Integer.parseInt(count.get()));
    }

    private CompletionStage<TestResult> runTest(UrlTest test){
        final Sink<UrlTest, CompletionStage<Long>> testSink =
                Flow.of(UrlTest.class)
                .mapConcat(t -> Collections.nCopies(t.getCount(), t.getUrl()))
                .mapAsync(countOfRequests, this::getRequestTime)
                .toMat(Sink.fold(0L, Long::sum), Keep.right());
    }

    private CompletionStage<Long> getRequestTime(String url){
        Instant requestStart = Instant.now();
        return asyncHttpClient.prepareGet(url).execute()
                .toCompletableFuture()
                .thenCompose(resp -> CompletableFuture.completedFuture(
                        Duration.between(requestStart, Instant.now()).getSeconds()
                ));

    }

    private HttpResponse saveAndCreateResponse(TestResult result) throws JsonProcessingException {
        storage.tell(result, ActorRef.noSender());
        return HttpResponse.create()
                .withStatus(StatusCodes.OK)
                .withEntity(ContentTypes.APPLICATION_JSON, ByteString.fromString(
                        new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(result)
                ));
    }

}
