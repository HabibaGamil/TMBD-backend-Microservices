package com.sumerge.userservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    @JsonProperty("error")
    public Boolean error;
    @JsonProperty("message")
    public String message;
}
