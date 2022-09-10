package base.anontations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Info {
    String createBy();
    String onDate();
    String lastModified();
    String desc() default "";
}
