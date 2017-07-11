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

package com.superman.fastokhttp.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassTypeReflect {


	//public static Type getSuperclassTypeParameter(Class<?> subclass){
	//    Type superclass = subclass.getGenericSuperclass();
	//    if (superclass instanceof Class){
	//        throw new RuntimeException("Missing type parameter.");
	//    }
	//    ParameterizedType parameterized = (ParameterizedType) superclass;
	//    return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
	//}

	public static Type getModelClazz(Class<?> subclass) {
		return getGenericType(0, subclass);
	}

	private static Type getGenericType(int index, Class<?> subclass) {
		Type superclass = subclass.getGenericSuperclass();
		if (!(superclass instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) superclass).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			throw new RuntimeException("Index outof bounds");
		}

		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return params[index];
	}
}
