package skislitsyn.testfw;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME) // Should be SOURCE for real test framework
@Target(METHOD)
public @interface Test {

}
