import java.util.List;

public class TestEntity {
    private String testCaseId;
    private List<Integer> faultMatrixData;
    private int lineNumber;

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public List<Integer> getFaultMatrixData() {
        return faultMatrixData;
    }

    public void setFaultMatrixData(List<Integer> faultMatrixData) {
        this.faultMatrixData = faultMatrixData;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
