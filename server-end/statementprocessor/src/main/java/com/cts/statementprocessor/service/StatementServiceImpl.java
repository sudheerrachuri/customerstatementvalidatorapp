package com.cts.statementprocessor.service;

import com.cts.statementprocessor.beans.ErrorRecord;
import com.cts.statementprocessor.beans.Result;
import com.cts.statementprocessor.beans.Statement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class StatementServiceImpl implements StatementService {

    @Override
    public Result parseStatement(List<Statement> statements) {
        Result result = new Result();

        validateStatments(statements);

        return null;
    }

    private List<ErrorRecord> validateStatments(List<Statement> statements) {
        List<ErrorRecord> duplicateRecords = getDuplicateRecords(statements);
        List<ErrorRecord> wrongMutationRecords = getWrongMutationRecords(statements);

        if (duplicateRecords.isEmpty() && wrongMutationRecords.isEmpty()) {
            return null;
        } else if (duplicateRecords.size() > 0 && wrongMutationRecords.size() > 0) {
            List<ErrorRecord> wrongMutationRecordsCopy = new ArrayList<>(wrongMutationRecords);
            wrongMutationRecordsCopy.removeAll(duplicateRecords);
            duplicateRecords.addAll(wrongMutationRecordsCopy);

            return duplicateRecords;
        } else if (duplicateRecords.size() > 0) {
            log.info("dup->" + duplicateRecords);
        } else if (wrongMutationRecords.size() > 0) {
            log.info("wrongMut->" + wrongMutationRecords);
        }
        return null;
    }

    private List<ErrorRecord> getWrongMutationRecords(List<Statement> statements) {
        List<ErrorRecord> wrongMutationRecord = new ArrayList<>();
        for (Statement statement : statements) {
            if (checkEndBalanceAndMutation(statement)) {
                wrongMutationRecord.add(getErrorRecord(statement));
            }
        }
        return wrongMutationRecord;
    }

    private boolean checkEndBalanceAndMutation(Statement statement) {
        return statement.getEndBalance().equals(statement.getStartBalance().add(statement.getMutation()));
    }

    private ErrorRecord getErrorRecord(Statement statement){
        ErrorRecord errRecord = new ErrorRecord();
        errRecord.setAccountNumber(statement.getAccountNumber());
        errRecord.setReference(statement.getReference());
        return errRecord;
    }

    private List<ErrorRecord> getDuplicateRecords(List<Statement> statements) {
        HashMap<Long, Statement> hMap = new HashMap<>();
        List<ErrorRecord> errRecords = new ArrayList<>();
        for (Statement statement : statements) {
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
