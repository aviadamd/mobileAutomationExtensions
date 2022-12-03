package base.rbi;

public class RbiProperty {

    public int getProperty() {return property;}

    public String getUserName() {return userName;}

    public String getUserNamePass() {return userNamePass;}

    public String getUserBankNumber() {return userBankNumber;}

    public String getUserBankBranch() {return userBankBranch;}

    public String getUserBankAccountNumber() {return userBankAccountNumber;}

    public TestStatus getTestStatus() { return testStatus;}

    private final int property;
    private final String userName;
    private final String userNamePass;
    private final String userBankNumber;
    private final String userBankBranch;
    private final String userBankAccountNumber;
    private final TestStatus testStatus;

    public RbiProperty(
            int property, String userName,
            String userNamePass, String userBankNumber,
            String userBankBranch, String userBankAccountNumber, TestStatus testStatus) {
        this.property = property;
        this.userName = userName;
        this.userNamePass = userNamePass;
        this.userBankNumber = userBankNumber;
        this.userBankBranch = userBankBranch;
        this.userBankAccountNumber = userBankAccountNumber;
        this.testStatus = testStatus;
    }

    public enum TestStatus {
        PASS,FAIL,SKIP,INFO
    }
}
