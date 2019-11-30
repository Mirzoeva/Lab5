package lab5;

import java.util.Optional;

public class TestResult {
    private final UrlTest test;
    private final Long avg;

    public TestResult(UrlTest test, Long avg){
        this.test = test;
        this.avg = avg;
    }

    public UrlTest getTest(){
        return test;
    }

    public Long getAvg(){
        return avg;
    }

    public Optional<TestResult> get(){
        return this.getAvg() != null ? Optional.of(this) : Optional.empty();
    }
}
