package com.cts.statementprocessor.exception;

import com.cts.statementprocessor.model.TransactionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomerStatementGlobalExceptionController {

    TransactionResponse transactionResponse = new TransactionResponse();
    @ExceptionHandler(JsonParseErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TransactionResponse handleJsonParseErrorException(final JsonParseErrorException ex) {
        log.error(ex.getMessage());
        transactionResponse.setResult("BAD_REQUEST");
        return transactionResponse;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public TransactionResponse handleException(final Throwable ex){
        log.error(ex.getMessage());
        transactionResponse.setResult("INTERNAL_SERVER_ERROR");
        return transactionResponse;
    }

}
