package lab4;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class TestData implements Serializable {
    private TestPackage parentPackage;
    private String testName;
    private String actualResult;
    private String expectedResult;
    private Object[] params;

    @JsonCreator
    public TestData(@JsonProperty("testName")String testName,
                      @JsonProperty("expectedResult")String expectedResult,
                      @JsonProperty("params")Object[] params){
        this.testName = testName;
        this.expectedResult = expectedResult;
        this.params = params;
    }

    public void setActualResult(String actualResult){
        this.actualResult = actualResult;
    }

    public void setParentPackage(TestPackage parentPackage){
        this.parentPackage = parentPackage;
    }

    public String getActualResult(){
        return this.actualResult;
    }

    public TestPackage getParentPackage(){
        return this.parentPackage;
    }

    public  String getTestName(){
        return this.testName;
    }

    public  String getExpectedResult(){
        return this.expectedResult;
    }

    public Object[] getParams(){
        return this.params;
    }



}
