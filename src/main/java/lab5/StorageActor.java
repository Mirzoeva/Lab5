package lab5;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import java.util.Map;
import java.util.TreeMap;

public class StorageActor  extends AbstractActor {
    private final Map<UrlTest, Long> storage;

    public StorageActor(){
        this.storage = new TreeMap<>();
    }

    @Override
    public Receive createReceive(){
        return receiveBuilder()
                .match(UrlTest.class, {

                })
                .match(TestResult.class, test -> {
                    getSender()
                            .tell(new ReturnUrlTestResult(new TestResult(test, storage.get(test))), ActorRef.noSender());
                } )
                .build();
    }
}
