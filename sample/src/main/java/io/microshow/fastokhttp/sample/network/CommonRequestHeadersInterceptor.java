package io.microshow.fastokhttp.sample.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.microshow.rxretrofit.interceptor.RequestHeadersInterceptor;

/**
 *device-lang:zh_CN
 * device-ip:192.168.1.100
 * os:2
 * city:gz
 * device-channel:3
 * device-lon:113.387317
 * cityId:1
 * device-address:%E4%B8%AD%E5%9B%BD%E5%B9%BF%E4%B8%9C%E7%9C%81%E5%B9%BF%E5%B7%9E%E5%B8%82%E5%A4%A9%E6%B2%B3%E5%8C%BA%E5%B2%91%E6%9D%91%E4%B8%9C%E8%A1%9737%E5%8F%B7
 * device-id:a0bc243bd2d2c497222507e70a4c93ef
 * app-version:1.6.0
 * registerSource:1
 * device-size:1080*1800
 * device-version:8.0.0
 * device-lat:23.168146
 * device-token:AuFUP0cbiBJBN_03NM-PClI2MoPRH38TF4KDOgPUa2GI
 * device-type:HONOR+BLN-AL10
 * device-net:3
 * device-type2:2
 */
public class CommonRequestHeadersInterceptor extends RequestHeadersInterceptor {

    @Override
    public Map<String, String> getCommonHeaders() {

        Map<String, String> HEADERS = new ConcurrentHashMap<>();

        HEADERS.put("device-lang","zh_CN");
        HEADERS.put("device-ip","192.168.1.100");
        HEADERS.put("os","2");
        HEADERS.put("city","gz");
        HEADERS.put("device-channel","3");
        HEADERS.put("device-lon","113.387317");
        HEADERS.put("device-lat","23.168146");
        HEADERS.put("cityId","1");
        HEADERS.put("device-address","%E4%B8%AD%E5%9B%BD%E5%B9%BF%E4%B8%9C%E7%9C%81%E5%B9%BF%E5%B7%9E%E5%B8%82%E5%A4%A9%E6%B2%B3%E5%8C%BA%E5%B2%91%E6%9D%91%E4%B8%9C%E8%A1%9737%E5%8F%B7");
        HEADERS.put("device-id","a0bc243bd2d2c497222507e70a4c93ef");
        HEADERS.put("app-version","1.6.0");
        HEADERS.put("registerSource","1");
        HEADERS.put("device-size","1080*1800");
        HEADERS.put("device-version","8.0.0");
        HEADERS.put("device-token","AuFUP0cbiBJBN_03NM-PClI2MoPRH38TF4KDOgPUa2GI");
        HEADERS.put("device-type","HONOR+BLN-AL10");
        HEADERS.put("device-net","3");
        HEADERS.put("device-type2","2");

        return HEADERS;
    }
}
