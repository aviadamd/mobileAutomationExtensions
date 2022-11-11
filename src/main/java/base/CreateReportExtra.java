package base;

import base.reports.testFilters.TestCategory;
import com.aventstack.extentreports.Status;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateReportExtra {
    Status[] reportStatus() default Status.FAIL;
    TestCategory[] filterBy() default TestCategory.NONE;
}
