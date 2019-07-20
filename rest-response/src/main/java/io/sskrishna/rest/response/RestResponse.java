package io.sskrishna.rest.response;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class RestResponse {
    private long timestamp = System.currentTimeMillis();
    private int status;
    private Object data;


    public static RestResponse init() {
        return new RestResponse();
    }

    public RestResponse data(Object data) {
        this.data = data;
        return this;
    }

    public RestResponse status(int status) {
        this.status = status;
        return this;
    }

    public ResponseEntity success() {
        ResponseEntity responseEntity;
        if (data == null) {
            responseEntity = new ResponseEntity(HttpStatus.valueOf(this.status));
        } else {
            responseEntity = new ResponseEntity(this, HttpStatus.valueOf(this.status));

        }
        return responseEntity;
    }
}
