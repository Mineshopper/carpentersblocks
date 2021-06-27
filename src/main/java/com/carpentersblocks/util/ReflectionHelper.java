package com.carpentersblocks.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.mojang.datafixers.util.Pair;

public class ReflectionHelper {

	/**
	 * Return all fields in class matching type.
	 * 
	 * @param returnType the return type
	 * @param clazz the class instance
	 * @return all field values of returnType in <code>clazz</code>
	 */
	public static <T> List<T> getStaticValues(Class<T> returnType, Class<?> clazz) {
		return Arrays
				.stream(clazz.getDeclaredFields())
				.filter(field -> field.getType().equals(returnType))
				.map(f -> {
					try {
						return (T) f.get(null);
					} catch (Exception e) { }
					return null;
				})
				.collect(Collectors.toList());
	}
	
	/**
	 * Invoke private method of input class using methodName
	 * and array of args.
	 * 
	 * @param object the source object
	 * @param methodName the method name
	 * @param args the arguments as {@link Pair} with class->instance
	 */
	public static void invokePrivateMethod(Object object, String methodName, Pair<Class<?>, Object> ... args) {
		Method method;
		Class<?>[] classes = Arrays.stream(args).map(a -> a.getFirst()).toArray(Class<?>[]::new);
		Object[] classArgs = Arrays.stream(args).map(a -> a.getSecond()).toArray(Object[]::new);
		try {
			method = object.getClass().getDeclaredMethod(methodName, classes);
			method.setAccessible(true);
			method.invoke(object, classArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
