package fr.fruityhedgeh0g.utilities.logging;

import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Journalise chaque appel de méthode du bean annoté : tentative et succès en DEBUG,
 * échec en ERROR avec l'exception, qui est ensuite propagée telle quelle.
 *
 * @see LoggingInterceptor
 */
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Logged {
}
