package base;

public interface ExceptionBearingAction <T> {
    T doAction() throws Exception;
}
