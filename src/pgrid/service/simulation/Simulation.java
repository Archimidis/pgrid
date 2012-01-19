package pgrid.service.simulation;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@BindingAnnotation
public @interface Simulation {
}
