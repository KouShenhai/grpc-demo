package org.laokouyun.demo;

@FunctionalInterface
public interface GrpcCallback<V> {
    V get() throws Exception;
}