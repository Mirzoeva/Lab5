package lab4;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class TestPackage implements Serializable {
    private String packageId;
    private String functionName;
    private String jsScript;
    private List<TestData> tests;

    public TestPackage(@JsonProperty("packageId") String packageId,
                       @JsonProperty("jsScript") String jsScript,
                       @JsonProperty("functionName") String functionName,
                       @JsonProperty("tests") List<TestData> tests){
        this.packageId = packageId;
        this.functionName = functionName;
        this.jsScript = jsScript;
        this.tests = tests;
    }

    public String getPackageId(){
        return this.packageId;
    }

    public String getFunctionName(){
        return this.functionName;
    }

    public String getJsScript(){
        return this.jsScript;
    }

    public List<TestData> getTests(){
        return this.tests;
    }

}
