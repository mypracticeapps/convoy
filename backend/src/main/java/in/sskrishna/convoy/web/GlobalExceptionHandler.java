package in.sskrishna.convoy.web;

import in.sskrishna.convoy.exception.InProgressException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InProgressException.class})
    public final ResponseEntity handleException(Exception ex, WebRequest request) {
        return new ResponseEntity(HttpStatus.TEMPORARY_REDIRECT);
    }
}