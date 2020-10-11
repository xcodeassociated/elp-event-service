package com.xcodeassociated.service.model.db.helpers;

@FunctionalInterface
public interface ValueComparable<T> {
    boolean compare(T t);
}
