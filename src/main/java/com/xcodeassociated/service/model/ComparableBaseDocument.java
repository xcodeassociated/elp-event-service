package com.xcodeassociated.service.model;

import com.xcodeassociated.service.model.helpers.ValueComparable;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class ComparableBaseDocument<T> extends BaseDocument implements ValueComparable<T> {}
