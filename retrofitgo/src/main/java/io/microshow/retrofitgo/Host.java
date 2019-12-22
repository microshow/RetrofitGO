package io.microshow.retrofitgo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 域名注解
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Host {

  String baseUrl();
}
