package com.cts.statementprocessor.util;

import com.cts.statementprocessor.model.CustomerStatement;
import com.cts.statementprocessor.model.ErrorRecord;
import com.cts.statementprocessor.model.TransactionResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Validationutil {

    public static TransactionResponse validateStatements(List<CustomerStatement> customerStatements) {
        log.info("inside validate statements");
        List<ErrorRecord> duplicateRecords = getDuplicateRecords(customerStatements);
        List<ErrorRecord> wrongMutationRecords = getWrongMutationRecords(customerStatements);
        return conditionsCheck(duplicateRecords,wrongMutationRecords);
    }


    private static List<ErrorRecord> getWrongMutationRecords(List<CustomerStatement> customerStatements) {
        log.info("inside wrong mutation record method");
        List<ErrorRecord> wrongMutationRecord = new ArrayList<>();
        if(customerStatements!= null)
            for (CustomerStatement statement : customerStatements) {
                if (!checkEndBalanceAndMutation(statement)) {
                    wrongMutationRecord.add(getErrorRecord(statement));
                }
            }
        return wrongMutationRecord;
    }

    private static TransactionResponse conditionsCheck(List<ErrorRecord> duplicateRecords,List<ErrorRecord> wrongMutationRecords) {
        if (bothDuplicateAndWrongMutationRecordsAreEmpty(duplicateRecords, wrongMutationRecords)) {
            return getTransactionResponse("SUCCESS", Collections.emptyList());
        }
        if (bothDuplicateAndWrongMutationRecordAreNotEmpty(duplicateRecords, wrongMutationRecords)) {
            List<ErrorRecord> errRecord =  filterDuplicatesFromBothRecords(duplicateRecords, wrongMutationRecords);
            return getTransactionResponse("DUPLICATE_REFERENCE _INCORRECT_END_BALANCE", errRecord);
        }
        if (onlyDuplicateRecords(duplicateRecords, wrongMutationRecords)) {
            return getTransactionResponse("DUPLICATE_REFERENCE", duplicateRecords);
        }

        return getTransactionResponse("INCORRECT_END_BALANCE", wrongMutationRecords);
    }

    private static List<ErrorRecord> filterDuplicatesFromBothRecords(List<ErrorRecord> duplicateRecords,List<ErrorRecord> wrongMutationRecords){
        Set<ErrorRecord> duplicateRecordsSet = new LinkedHashSet<>(duplicateRecords);
        duplicateRecordsSet.addAll(wrongMutationRecords);
        List<ErrorRecord> filteredRecord = new ArrayList<>(duplicateRecordsSet);
        return filteredRecord;
    }
    private static boolean bothDuplicateAndWrongMutationRecordAreNotEmpty(List<ErrorRecord> duplicateRecords, List<ErrorRecord> wrongMutationRecords) {
        return !duplicateRecords.isEmpty() && !wrongMutationRecords.isEmpty();
    }

    private static boolean bothDuplicateAndWrongMutationRecordsAreEmpty(List<ErrorRecord> duplicateRecords, List<ErrorRecord> wrongMutationRecords) {
            return duplicateRecords.isEmpty() && wrongMutationRecords.isEmpty();
    }

    private static boolean onlyDuplicateRecords(List<ErrorRecord> duplicateRecords,List<ErrorRecord> wrongMutationRecords){
        return !duplicateRecords.isEmpty() && wrongMutationRecords.isEmpty();
    }

    private static TransactionResponse getTransactionResponse(String errCode,List<ErrorRecord> errorRecords){
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setErrorRecords(errorRecords);
        transactionResponse.setResult(errCode);
        return  transactionResponse;
    }

    private static boolean checkEndBalanceAndMutation(CustomerStatement statement) {
        return statement.getEndBalance().equals(statement.getStartBalance().add(statement.getMutation()));
    }

    private static ErrorRecord getErrorRecord(CustomerStatement statement){
        ErrorRecord errRecord = new ErrorRecord();
        errRecord.setAccountNumber(statement.getAccountNumber());
        errRecord.setReference(statement.getReference());
        return errRecord;
    }

    private static List<ErrorRecord> getDuplicateRecords(List<CustomerStatement> customerStatements) {
        log.info("inside get duplicate records method");
        HashMap<Long, CustomerStatement> hMap = new HashMap<>();
        List<ErrorRecord> errRecords = new ArrayList<>();
        if(customerStatements!= null)
            for (CustomerStatement statement : customerStatements) {
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
