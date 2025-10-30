package com.app.taskmanager.controller;

import com.app.taskmanager.EntityNotFoundException;
import com.app.taskmanager.dto.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * Global exception handler that centralizes error handling for all REST controllers.
 * <p>
 * This class uses reactive {@link Mono} responses and returns consistent error responses
 * wrapped in {@link com.app.taskmanager.dto.response.ResponseDto}.
 * Each exception type is handled separately and returns an appropriate HTTP status code.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles {@link EntityNotFoundException} by returning a 404 NOT FOUND response.
     *
     * @param e the exception indicating that an entity was not found
     * @return a {@code Mono<ResponseDto>} containing the error message
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseDto<String>> entityNotFound(EntityNotFoundException e) {
        log.warn("Entity not found: ", e);
        return Mono.just(new ResponseDto<>(e.getMessage()));
    }

    /**
     * Handles validation errors thrown during request binding.
     *
     * @param ex the exception containing validation errors
     * @return a {@code Mono<ResponseDto>} containing concatenated validation error messages
     */
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseDto<String>> handleValidationException(WebExchangeBindException ex) {
        String message = ex.getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Validation error: {}", message);
        return Mono.just(new ResponseDto<>(message));
    }

    /**
     * Handles {@link IllegalArgumentException} by returning a 400 BAD REQUEST response.
     *
     * @param e the thrown exception
     * @return a {@code Mono<ResponseDto>} containing the error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseDto<String>> illegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal argument: ", e);
        return Mono.just(new ResponseDto<>(e.getMessage()));
    }

    /**
     * Handles {@link MethodArgumentTypeMismatchException}, typically caused by invalid enum or type parameters.
     *
     * @param ex the thrown exception
     * @return a {@code Mono<ResponseDto>} containing the error message
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseDto<String>> handleEnumMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Method argument type mismatch exception: ", ex);
        return Mono.just(new ResponseDto<>(ex.getMessage()));
    }

    /**
     * Handles {@link ResponseStatusException} by returning its reason as the response message.
     *
     * @param ex the thrown exception
     * @return a {@code Mono<ResponseDto>} containing the reason from the exception
     */
    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseDto<String>> handleResponseStatusException(ResponseStatusException ex) {
        log.warn("ResponseStatusException: {}", ex.getReason(), ex);
        return Mono.just(new ResponseDto<>(ex.getReason()));
    }

    /**
     * Handles {@link ServerWebInputException}, which occurs when input cannot be properly parsed.
     *
     * @param ex the thrown exception
     * @return a {@code Mono<ResponseDto>} with a generic invalid input message
     */
    @ExceptionHandler(ServerWebInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseDto<String>> handleServerWebInput(ServerWebInputException ex) {
        log.warn("ServerWebInputException: {}", ex.getReason(), ex);
        return Mono.just(new ResponseDto<>("Invalid input format"));
    }

    /**
     * Handles all other unhandled exceptions by returning a 500 INTERNAL SERVER ERROR response.
     *
     * @param e the thrown exception
     * @return a {@code Mono<ResponseDto>} containing the exception message
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ResponseDto<String>> GlobalExceptionHandler(RuntimeException e) {
        log.warn("Exception occurred: ", e);
        return Mono.just(new ResponseDto("Internal server error"));
    }
}
