package base.excelReader;

public class ClassPogo {
    private String id;
    private String userName;
    private String userPass;
    public ClassPogo(String id, String userName, String userPass) {
        this.id = id;
        this.userName = userName;
        this.userPass = userPass;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id;}
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName;}
    public String getUserPass() { return userPass; }
    public void setUserPass(String userPass) { this.userPass = userPass; }

    @Override
    public String toString() {
        return "ClassPogo{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", userPass='" + userPass + '\'' +
                '}';
    }

}
