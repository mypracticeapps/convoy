package io.sskrishna.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class  ErrorDetail {
    private String code;
    private String message;
    private String devMessage;
    private String cause;

    public ErrorDetail(String code, String message, String devMessage) {
        this.code = code;
        this.message = message;
        this.devMessage = devMessage;
    }
}