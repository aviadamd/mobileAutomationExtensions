package base.anontations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestTarget {
    String testId() default "";
    String[] author() default "unknownAuthor";
    CategoryType[] category() default CategoryType.SANITY;
    String suiteName() default "";
}
