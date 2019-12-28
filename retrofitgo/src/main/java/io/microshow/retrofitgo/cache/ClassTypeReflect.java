package io.microshow.retrofitgo.cache;

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
