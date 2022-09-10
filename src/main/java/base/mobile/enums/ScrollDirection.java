package base.mobile.enums;

public enum ScrollDirection {
    LEFT("LEFT"),
    RIGHT("RIGHT"),
    DOWN_LARGE("DOWN_LARGE"),
    DOWN("DOWN"),
    UP("UP"),
    UP_LITTLE("UP_LITTLE"),
    UP_LARGE("UP_LARGE"),
    DOWN_LITTLE("DOWN_LITTLE");
    private final String scrollDirection;
    ScrollDirection(String scrollDirection) { this.scrollDirection = scrollDirection; }
    public String getScrollDirection() { return scrollDirection; }
}
