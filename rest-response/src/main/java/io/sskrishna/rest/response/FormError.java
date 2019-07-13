package io.sskrishna.rest.response;

import lombok.Data;

import java.util.Optional;

public class FormError extends RestError {

    private final ErrorCodeLookup errorCodeLookup;

    FormError(ErrorCodeLookup errorCodeLookup) {
        super(errorCodeLookup);
        this.errorCodeLookup = errorCodeLookup;
    }

    @Data
    public static class FormErrorDetail extends ErrorDetail {
        private String field;
        private Object data;

        public FormErrorDetail(String field, Object data, String code, String message, String devMessage) {
            super(code, message, devMessage);
            this.field = field;
            this.data = data;
        }
    }

//    public void rejectField(String field, String code) {
//        ErrorDetail errorCode = this.errorCodeLookup.getErrorCode(code);
//        this.addExceptionDetail(field, null, errorCode.getCode(), errorCode.getMessage(), errorCode.getDevMessage());
//    }

    public void rejectField(String field, Object data, String code) {
        ErrorDetail errorCode = this.errorCodeLookup.getErrorCode(code);
        this.addExceptionDetail(field, data, errorCode.getCode(), errorCode.getMessage(), errorCode.getDevMessage());
    }

    public void rejectIfEmpty(String subject, String field, String code) {
        if (subject == null || subject.isEmpty()) {
            this.rejectField(field, null, code);
        }
    }

    public void rejectIfEmpty(Optional subject, String field, String code) {
        if (subject == null || !subject.isPresent()) {
            this.rejectField(field, null, code);
        }
    }

    public boolean containsFieldError(String field) {
        for (ErrorDetail errorDetail : super.getErrors()) {
            FormErrorDetail detail = (FormErrorDetail) errorDetail;
            if (detail.field.equals(field)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsErrorCode(String code) {
        for (ErrorDetail errorDetail : super.getErrors()) {
            FormErrorDetail detail = (FormErrorDetail) errorDetail;
            if (detail.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public void addExceptionDetail(String field, Object data, String code, String message, String devMessage) {
        ErrorDetail detail = new FormError.FormErrorDetail(field, data, code, message, devMessage);
        super.addErrorDetail(detail);
    }
}