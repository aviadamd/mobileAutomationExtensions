package base.anontations;

public enum CategoryType {
    REGRESSION("Regression"),
    SANITY("Sanity");

    private String type;
    public void setType(String type) { this.type = type;}

    CategoryType(String type) { this.type = type; }
    public String getType() { return type; }
}
