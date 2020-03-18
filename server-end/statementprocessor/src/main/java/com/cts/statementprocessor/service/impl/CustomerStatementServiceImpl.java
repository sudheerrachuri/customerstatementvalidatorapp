package com.cts.statementprocessor.service.impl;

import com.cts.statementprocessor.model.ErrorRecord;
import com.cts.statementprocessor.model.TransactionResponse;
import com.cts.statementprocessor.model.CustomerStatement;
import com.cts.statementprocessor.exception.JsonParseErrorException;
import com.cts.statementprocessor.service.CustomerStatementService;
import com.cts.statementprocessor.util.Validationutil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This service class will validate the logic of rabobank statement
 * @author Rachuri Sudheer
 */
@Service
@Slf4j
public class CustomerStatementServiceImpl implements CustomerStatementService {

    TransactionResponse transactionResponse = new TransactionResponse();

    /**
     * This method will parse customer statement
     * @param customerStatements rabobank customer statement
     * @return transaction response
     */
    @Override
    public TransactionResponse processCustomerStatement(List<CustomerStatement> customerStatements) {
        log.info("inside parse customer statement method");
        try {
            transactionResponse = Validationutil.validateStatements(customerStatements);
            return transactionResponse;
        }catch(Exception ex){
            throw new JsonParseErrorException();
        }
    }

}
