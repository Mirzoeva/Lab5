package lab5;

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
}
