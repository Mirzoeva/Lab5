package lab4;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StorageActor extends AbstractActor {

    private Map<String, ArrayList<TestData>> store = new HashMap<>();

    private void putTest(TestData testData){
        String packageId = testData.getParentPackage().getPackageId();
        if (this.store.containsKey(packageId)){
            this.store.get(packageId).add(testData);
        } else {
            ArrayList<TestData> tests = new ArrayList<>();
            tests.add(testData);
            this.store.put(packageId, tests);
        }
    }
//
//    private ArrayList<TestData> getTests(String packageId) throws Exception{
//        if (this.store.containsKey(packageId)){
//            return this.store.get(packageId);
//        } else {
//            throw  new Exception("No such package");
//        }
//    }
//
//    private RequestAnswers makeResults(String packageId){
//        ArrayList<TestResult> testAnswers = new ArrayList<>();
//        try{
//            for(TestData test: this.getTests(packageId)){
//                String actualResult = test.getActualResult();
//                String rightResult = test.getExpectedResult();
//                TestResult testResult = new TestResult(
//                        rightResult,
//                        actualResult,
//                        actualResult.equals(rightResult)
//                );
//                testAnswers.add(testResult);
//            }
//            return new RequestAnswers(packageId, testAnswers);
//        } catch (Exception exception){
//            return new RequestAnswers("No such package", testAnswers);
//        }
//    }

    private RequestAnswers makeResults(String packageId) {
        ArrayList<TestResult> testAnswers = new ArrayList<>();
        if (this.store.containsKey(packageId)) {
            for (TestData test : this.store.get(packageId)) {
                String actualResult = test.getActualResult();
                String rightResult = test.getExpectedResult();
                TestResult testResult = new TestResult(
                        rightResult,
                        actualResult,
                        actualResult.equals(rightResult)
                );
                testAnswers.add(testResult);
            }
            return new RequestAnswers(packageId, testAnswers);
        } else {
            return new RequestAnswers("No such package", testAnswers);
        }
    }




    @Override
    public Receive createReceive(){
        return ReceiveBuilder.create()
                .match(TestData.class, test -> this.putTest(test))
                .match(String.class, id -> sender().tell(makeResults(id), self()))
                .build();
    }
}