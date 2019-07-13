package io.sskrishna.rest.response;

import lombok.Data;

@Data
public class RestException extends RuntimeException {
    private final RestError restError;

    public RestException(RestError restError) {
        this.restError = restError;
    }
}