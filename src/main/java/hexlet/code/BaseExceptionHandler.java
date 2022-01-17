package hexlet.code;

import java.util.List;
import java.util.NoSuchElementException;

import com.rollbar.notifier.Rollbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ControllerAdvice
public final class BaseExceptionHandler {
    @Autowired
    private Rollbar rollbar;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String internalExceptionHandler(Exception exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String noSuchElementException(NoSuchElementException exception) {
        rollbar.error(exception);
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ObjectError> validationExceptionHandler(MethodArgumentNotValidException exception) {
        rollbar.error(exception);
        return exception.getAllErrors();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public String accessDeniedException(AccessDeniedException exception) {
        rollbar.error(exception);
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UsernameNotFoundException.class)
    public String userNotFoundException(UsernameNotFoundException exception) {
        rollbar.error(exception);
        return exception.getMessage();
    }
}
