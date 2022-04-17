package cc.woverflow.debugify.fixes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BugFix {
    String id();

    Env env();

    boolean enabled() default true;

    enum Env {
        CLIENT,
        SERVER
    }
}
