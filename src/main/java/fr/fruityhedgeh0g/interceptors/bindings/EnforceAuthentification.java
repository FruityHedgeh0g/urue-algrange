package fr.fruityhedgeh0g.interceptors.bindings;

import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@EnforceAuthentification
@Retention(RetentionPolicy.RUNTIME)
public @interface EnforceAuthentification {
}
