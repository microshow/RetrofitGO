package io.microshow.rxretrofit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 域名注解
 *
 * @author sqlva
 * @date 2018-07-18
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Host {

  String baseUrl();
}
