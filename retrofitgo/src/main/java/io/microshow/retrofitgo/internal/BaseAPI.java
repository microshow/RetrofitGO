package io.microshow.retrofitgo.internal;

/**
 * Created by Super on 2019/12/25.
 */
public interface BaseAPI {

    /**
     * 只查询网络数据
     */
    String CACHE_POLICY_MODE_POLICY_ONLY_NETWORK = "CACHE_POLICY_MODE:POLICY_ONLY_NETWORK";

    /**
     * 只查询缓存数据
     */
    String CACHE_POLICY_MODE_POLICY_ONLY_CACHED = "CACHE_POLICY_MODE:POLICY_ONLY_CACHED";

    /**
     * 先查询网络数据，如果没有，再查询本地缓存
     */
    String CACHE_POLICY_MODE_POLICY_NETWORK_ELSE_CACHED = "CACHE_POLICY_MODE:POLICY_NETWORK_ELSE_CACHED";

    /**
     * 先查询本地缓存,不管有没缓存,紧接着都会查询网络数据，此策略会回调两次响应
     * 注：用户体验更好
     */
    String CACHE_POLICY_MODE_POLICY_CACHED_AND_NETWORK = "CACHE_POLICY_MODE:POLICY_CACHED_AND_NETWORK";
}
