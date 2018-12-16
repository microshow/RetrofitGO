/*
 * Copyright (C) 2017 zhichao.huang, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.microshow.fastokhttp.core;

import io.microshow.fastokhttp.BuildConfig;

/**
 * 常量配置
 * @author zhichao.huang
 *
 */
public class Constants {
	
    public static boolean DEBUG = BuildConfig.DEBUG;
    
    /**
     * Http请求超时时间
     */
    public static final int REQ_TIMEOUT = 30000;
    
    /**
     * 线程池大小
     */
    public static int net_pool_size = 10;
    
    /**
     * 网络错误是否捕捉异常
     */
    public static boolean net_error_try = false;
}
