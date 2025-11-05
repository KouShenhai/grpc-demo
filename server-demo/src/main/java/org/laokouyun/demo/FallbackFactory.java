package org.laokouyun.demo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class FallbackFactory {

    private static final Map<Class<?>, Object> FALLBACK_MAP = new ConcurrentHashMap<>();

    private FallbackFactory() {}

    @SuppressWarnings("unchecked")
    public static <T> T getFallback(Class<T> clazz) {
        return (T) FALLBACK_MAP.get(clazz);
    }

    public static <T> void registerFallback(Class<?> clazz, T fallback) {
        FALLBACK_MAP.put(clazz, fallback);
    }

}
