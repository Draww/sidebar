package me.crune.sidebar.api.provider;

public interface Provider<T, K> {

    K get(T t);
}
