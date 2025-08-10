package com.huysama.builderCore.repositories.base;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repository
public @interface Hamatory {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";
}
