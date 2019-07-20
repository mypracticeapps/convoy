package io.sskrishna.rest.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Data
public class RestError extends RestResponse {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private transient final ErrorCodeLookup errorCodeLookup;

    private String code;
    private String message;
    private String devMessage;
    private Set<ErrorDetail> errors = new HashSet<>();

    RestError(ErrorCodeLookup errorCodeLookup) {
        this.errorCodeLookup = errorCodeLookup;
    }

    public boolean hasErrors() {
        return this.errors.size() > 0;
    }

    public void addErrorDetail(String code, String message, String devMessage) {
        ErrorDetail detail = new ErrorDetail(code, message, devMessage);
        this.addErrorDetail(detail);
    }

    public void addErrorDetail(ErrorDetail detail) {
        this.errors.add(detail);
    }

    public void throwError(int status, String code) {
        ErrorDetail errorDetail = this.errorCodeLookup.getErrorCode(code);
        this.throwError(status, code, errorDetail.getMessage());
    }

    public void throwError(int status, String code, String msg) {
        ErrorDetail errorDetail = this.errorCodeLookup.getErrorCode(code);
        this.setStatus(status);
        this.setCode(code);
        this.setMessage(msg);
        this.setDevMessage(errorDetail.getDevMessage());
        throw new RestException(this);
    }

    public void throwIfContainsErrors(int status, String code){
        if(this.hasErrors()){
            this.throwError(status, code);
        }
    }

    public void throwIfContainsErrors(int status, String code, String msg){
        if(this.hasErrors()){
            this.throwError(status, code, msg);
        }
    }
}
