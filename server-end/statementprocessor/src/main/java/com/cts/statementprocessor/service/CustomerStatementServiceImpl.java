package com.cts.statementprocessor.service;

import com.cts.statementprocessor.beans.ErrorRecord;
import com.cts.statementprocessor.beans.TransactionResponse;
import com.cts.statementprocessor.beans.CustomerStatement;
import com.cts.statementprocessor.exception.JsonParseErrorException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Statement;
import java.nio.charset.StandardCharsets;
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
     * @param statements rabobank statement file
     * @return transaction response
     */
    @Override
    public TransactionResponse parseCustomerStatement(List<CustomerStatement> statements) {
        log.info("inside parse customer statement method");
        try {
            transactionResponse = validateStatments(statements);
            return transactionResponse;
        }catch(Exception ex){
            throw new JsonParseErrorException();
        }
    }

    private TransactionResponse validateStatments(List<CustomerStatement> jsonDataFromFile) {
        log.info("inside validate statements");
        List<ErrorRecord> duplicateRecords = getDuplicateRecords(jsonDataFromFile);
        List<ErrorRecord> wrongMutationRecords = getWrongMutationRecords(jsonDataFromFile);

        if (duplicateRecords.size() > 0 && wrongMutationRecords.isEmpty()) {
            log.info("dup->" + duplicateRecords);
            transactionResponse.setErrorRecords(duplicateRecords);
            transactionResponse.setResult("DUPLICATE_REFERENCE");
            return  transactionResponse;
        } else if(duplicateRecords.isEmpty() && wrongMutationRecords.size() > 0) {
            log.info("wrongMut->" + wrongMutationRecords);
            transactionResponse.setErrorRecords(wrongMutationRecords);
            transactionResponse.setResult("INCORRECT_END_BALANCE");
            return transactionResponse;
        } else if (duplicateRecords.size() > 0 && wrongMutationRecords.size() > 0) {
            List<ErrorRecord> wrongMutationRecordsCopy = new ArrayList<>(wrongMutationRecords);
            wrongMutationRecordsCopy.removeAll(duplicateRecords);
            duplicateRecords.addAll(wrongMutationRecordsCopy);
            transactionResponse.setErrorRecords(duplicateRecords);
            transactionResponse.setResult("DUPLICATE_REFERENCE _INCORRECT_END_BALANCE");
            log.info("dup&incorrect "+String.valueOf(duplicateRecords));
            return transactionResponse;
        }else if (duplicateRecords.isEmpty() && wrongMutationRecords.isEmpty())
            log.info("success");
            transactionResponse.setResult("SUCCESS");
            return transactionResponse;
    }

    private List<ErrorRecord> getWrongMutationRecords(List<CustomerStatement> jsonDataFromFile) {
        log.info("inside wrong mutation record method");
        List<ErrorRecord> wrongMutationRecord = new ArrayList<>();
        if(jsonDataFromFile!= null)
        for (CustomerStatement statement : jsonDataFromFile) {
            if (!checkEndBalanceAndMutation(statement)) {
                wrongMutationRecord.add(getErrorRecord(statement));
            }
        }
        return wrongMutationRecord;
    }

    private boolean checkEndBalanceAndMutation(CustomerStatement statement) {
        return statement.getEndBalance().equals(statement.getStartBalance().add(statement.getMutation()));
    }

    private ErrorRecord getErrorRecord(CustomerStatement statement){
        ErrorRecord errRecord = new ErrorRecord();
        errRecord.setAccountNumber(statement.getAccountNumber());
        errRecord.setReference(statement.getReference());
        return errRecord;
    }

    private List<ErrorRecord> getDuplicateRecords(List<CustomerStatement> jsonDataFromFile) {
        log.info("inside get duplicate records method");
        HashMap<Long, CustomerStatement> hMap = new HashMap<>();
        List<ErrorRecord> errRecords = new ArrayList<>();
        if(jsonDataFromFile!= null)
        for (CustomerStatement statement : jsonDataFromFile) {
            Long ref = statement.getReference();
            if (hMap.containsKey(ref)) {
                errRecords.add(getErrorRecord(statement));
            } else {
                hMap.put(statement.getReference(), statement);
            }
        }
        return errRecords;
    }


}
