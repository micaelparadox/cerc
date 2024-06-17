package com.cerc.tio.financialasset.cdp.notifier.config.anotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DomainType {
    String value();
}