package com.xcodeassociated.service.model.helpers;

import com.xcodeassociated.service.model.ComparableBaseDocument;

import java.util.Collection;
import java.util.stream.Collectors;


public class CollectionsByValueComparator {
    public static <T extends ComparableBaseDocument> boolean areCollectionsSame(Collection<T> left, Collection<T> right) {
        return left.size() == right.size()
                && right.size() == left.stream()
                        .map(e -> CollectionsByValueComparator.isElementInCollection(e, right))
                        .filter(e -> e.equals(true))
                        .collect(Collectors.toList()).size();
    }

    public static <T extends ComparableBaseDocument> boolean isElementInCollection(T element, Collection<T> elements) {
        boolean hasElement = false;
        for (T elem : elements) {
            if (elem.compare(element)) {
                hasElement = true;
            }
        }
        return hasElement;
    }
}
