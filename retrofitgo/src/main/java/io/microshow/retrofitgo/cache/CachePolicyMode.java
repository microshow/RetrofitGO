package io.microshow.retrofitgo.cache;

/**
 * 缓存策略
 * Created by Super on 2019/12/24.
 */
public enum CachePolicyMode {

    /**
     * 只查询网络数据
     */
    POLICY_ONLY_NETWORK,

    /**
     * 先查询网络数据，如果没有，再查询本地缓存
     */
    POLICY_NETWORK_ELSE_CACHE,

    /**
     * 先查询本地缓存,不管有没缓存,紧接着都会查询网络数据，此策略会回调两次响应
     * 注：用户体验更好
     */
    POLICY_CACHE_AND_NETWORK,

}
