package com.cerc.tio.financial.asset.tr.layout.middleware.common.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Stopwatch {

    String prefix() default "tio-middleware-cdp-tag-merchant-api";
}
