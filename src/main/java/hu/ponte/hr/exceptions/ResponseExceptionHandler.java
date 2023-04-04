package hu.ponte.hr.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String UNRESOLVED_ERROR = "Unresolved problem happened during encoding file or checking " +
            "private/public keys";
    @ExceptionHandler(FileSizeLimitException.class)
    public ResponseEntity<String> handleFileSizeLimitException(FileSizeLimitException exception) {
        log.warn(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOErrors(IOException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NoSuchAlgorithmException.class, InvalidKeySpecException.class})
    public ResponseEntity<String> handleEncryptErrors(Exception exception) {
        log.error(UNRESOLVED_ERROR);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
