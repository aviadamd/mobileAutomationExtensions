//package base;
//
//public class Wrapper {
//
//    public static <T> T unchecked(final ExceptionBearingAction<T> template, Consumer<Exception> exceptionConsumer) {
//        T results = null;
//        try {
//            results = template.doAction();
//        } catch (Exception ex) {
//            exceptionConsumer.accept(ex);
//        }
//        return results;
//    }
//}
