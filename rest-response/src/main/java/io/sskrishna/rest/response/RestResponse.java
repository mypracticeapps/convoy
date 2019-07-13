package io.sskrishna.rest.response;

import lombok.Data;

@Data
public class RestResponse {
    private long timestamp = System.currentTimeMillis();
    private int status;
    private Object data;
}