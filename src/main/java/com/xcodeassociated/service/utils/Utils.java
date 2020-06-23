package com.xcodeassociated.service.utils;

import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Utils {
    private Utils() {
        throw new ServiceException(ErrorCode.E002, "Utils should not be instantiated");
    }

    public static <T> boolean anyNonNull(T[] data) {
        if (data == null) {
            return false;
        }

        List<T> collection = List.of(data);
        return collection.stream().filter(Objects::nonNull).count() == collection.size();
    }

    public static <T> boolean anyNonNull(Collection<T> data) {
        if (data == null) {
            return false;
        } else {
            return data.stream().filter(Objects::nonNull).count() == data.size();
        }
    }
}
