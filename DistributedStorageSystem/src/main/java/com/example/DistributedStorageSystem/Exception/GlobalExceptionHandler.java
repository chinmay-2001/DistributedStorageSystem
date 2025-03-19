package com.example.DistributedStorageSystem.Exception;

import org.apache.coyote.BadRequestException;
import org.bouncycastle.crypto.agreement.srp.SRP6Client;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle Specific Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex){
        return buildErrorResponose(ex.getMessage(),HttpStatus.NOT_FOUND);
    }

    //Handle Generic Exception

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public  ResponseEntity<Map<String,Object>> handleGenericException(Exception ex){
        return buildErrorResponose("An unexpected Error occur",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({JDBCConnectionException.class, CannotCreateTransactionException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<Map<String,Object>> handleDatabaseConnectionException(Exception ex){
        return buildErrorResponose("Database is unavilable.Please Try again later",HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public   ResponseEntity<Map<String,Object>> handleDataAccessException(DataAccessException ex){
        return buildErrorResponose("DataBase exception occur",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public   ResponseEntity<Map<String,Object>> handleBadRequestException(BadRequestException ex){
        return buildErrorResponose(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public   ResponseEntity<Map<String,Object>> handleUnauthorizedException(HttpClientErrorException.Unauthorized ex){
        return buildErrorResponose(ex.getMessage(),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public  ResponseEntity<Map<String,Object>> handleRunTimeException(RuntimeException ex){
        return buildErrorResponose(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponose(String message, HttpStatus httpStatus) {
        Map<String,Object> errorDetail=new HashMap<>();
        errorDetail.put("TimeStamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        errorDetail.put("status",httpStatus.value());
        errorDetail.put("error",httpStatus.getReasonPhrase());
        errorDetail.put("message",message);
        return new ResponseEntity<>(errorDetail,httpStatus);
    }

}
