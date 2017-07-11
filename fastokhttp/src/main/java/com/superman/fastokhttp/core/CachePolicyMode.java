package com.superman.fastokhttp.core;

/**
 * 缓存策略
 * @author zhichao.huang
 *
 */
public enum CachePolicyMode {
	
	/**
	 * 只查询网络数据
	 */
	POLICY_ONLY_NETWORK,
	
	/**
	 * 只查询缓存数据
	 */
	POLICY_ONLY_CACHED,
	
	/**
	 * 先查询网络数据，如果没有，再查询本地缓存
	 */
	POLICY_NETWORK_ELSE_CACHED,
	
	/**
	 * 先查询本地缓存,不管有没缓存,紧接着都会查询网络数据，此策略会回调两次响应
	 * 注：用户体验更好
	 */
	POLICY_CACHED_AND_NETWORK,

}
