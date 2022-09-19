package base.listeners;

import base.driversManager.MobileManager;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MobileAnnotationRetryListener
        extends MobileManager implements IAnnotationTransformer {

    @Override
    public void transform(
            ITestAnnotation annotation, Class testClass,
            Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(MobileRetryAnalyzer.class);
    }
}
