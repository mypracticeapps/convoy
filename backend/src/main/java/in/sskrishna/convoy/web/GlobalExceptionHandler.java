package in.sskrishna.convoy.web;

import io.sskrishna.rest.response.RestError;
import io.sskrishna.rest.response.RestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RestException.class})
    public final ResponseEntity handleRestException(Exception ex, WebRequest request) {
        RestException exception = (RestException) ex;
        RestError restError = exception.getRestError();
        return new ResponseEntity(restError, new HttpHeaders(), HttpStatus.valueOf(restError.getStatus()));
    }
}