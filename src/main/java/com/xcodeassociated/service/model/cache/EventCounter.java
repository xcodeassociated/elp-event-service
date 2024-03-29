package com.xcodeassociated.service.model.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCounter implements Serializable {
    private String key;
    private Integer count;
}
