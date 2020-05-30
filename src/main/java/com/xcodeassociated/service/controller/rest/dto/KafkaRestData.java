package com.xcodeassociated.service.controller.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class KafkaRestData {
    @JsonProperty(value = "data", required = true)
    private String data;
}
