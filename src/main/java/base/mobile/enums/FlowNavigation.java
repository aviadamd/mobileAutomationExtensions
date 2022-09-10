package base.mobile.enums;

public enum FlowNavigation {
    FIRST(0),
    SECOND(1),
    THIRD(2);

    private final int number;
    FlowNavigation(int number) {
            this.number = number;
    }
    public int getNumber() {
        return number;
    }

}
