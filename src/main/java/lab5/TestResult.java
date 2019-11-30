package lab5;

import java.util.Optional;

public class TestResult {
    private final UrlTest test;
    private final Long middleValue;

    public TestResult(UrlTest test, Long middleValue){
        this.test = test;
        this.middleValue = middleValue;
    }

    public UrlTest getTest(){
        return test;
    }

    public Long getMiddleValue(){
        return middleValue;
    }

    public Optional<TestResult> get(){
        return this.getMiddleValue() != null ? Optional.of(this) : Optional.empty();
    }
}
