package com.java_projects.user_management.advisor;

import com.java_projects.user_management.exceptions.*;
import com.java_projects.user_management.utils.ErrorMessage;
import jakarta.persistence.NoResultException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@ResponseStatus
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InternalServerException.class)
    public @ResponseBody ResponseEntity<ErrorMessage> handleInternalServerException(
            final InternalServerException ex,
            final WebRequest request
    ) {
        return getExceptionResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody ResponseEntity<ErrorMessage> handleNotFoundException(
            final NotFoundException ex,
            final WebRequest request
    ) {
        return getExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessage> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return getExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotCreatedException.class)
    public ResponseEntity<ErrorMessage> handleUserNotCreatedException(final UserNotCreatedException ex,
                                                                      final WebRequest request) {
        return getExceptionResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserUpdateFailedException.class)
    public ResponseEntity<ErrorMessage> handleUserUpdateFailedException(final UserUpdateFailedException ex,
                                                                      final WebRequest request) {
        return getExceptionResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private ResponseEntity<ErrorMessage> getExceptionResponse(final String message, HttpStatus httpStatus) {
        ErrorMessage errorMessage = new ErrorMessage(message);
        return ResponseEntity.status(httpStatus).body(errorMessage);
    }
}
