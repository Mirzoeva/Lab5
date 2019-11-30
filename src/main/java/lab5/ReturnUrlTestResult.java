package lab5;

import java.util.Optional;

public class ReturnUrlTestResult {
    private final TestResult result;

    public ReturnUrlTestResult(TestResult result){
        this.result = result;
    }

    public Optional<TestResult> get(){
        return result.getAvg() != null ? Optional.of(result) : Optional.empty();
    }
}
