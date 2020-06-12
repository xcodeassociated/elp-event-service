package com.xcodeassociated.service.model.helpers;

@FunctionalInterface
public interface ValueComparable<T> {
    boolean compare(T t);
}
