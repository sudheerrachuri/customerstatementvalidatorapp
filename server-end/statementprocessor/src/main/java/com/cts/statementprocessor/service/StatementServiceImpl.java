package com.cts.statementprocessor.service;

import com.cts.statementprocessor.beans.ErrorRecord;
import com.cts.statementprocessor.beans.Result;
import com.cts.statementprocessor.beans.Statement;
import lombok.extern.slf4j.Slf4j;
import org.decimal4j.util.DoubleRounder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class StatementServiceImpl implements StatementService {

    @Override
    public Result parseStatement(List<Statement> statements) {
        Result result = new Result();

        valdiateStatments(statements);
        return null;
    }

    private List<ErrorRecord> valdiateStatments(List<Statement> statements) {
        List<ErrorRecord> duplicateRecords = getDuplicateRecords(statements);
        List<ErrorRecord> wrongMutaionRecords = getWrongMutaionRecords(statements);

        if (duplicateRecords.isEmpty() && duplicateRecords.isEmpty()) {
            return null;
        } else if (duplicateRecords.size() > 0 && wrongMutaionRecords.size() > 0) {
            List<ErrorRecord> wrongMutaionRecordsCopy = new ArrayList<>(wrongMutaionRecords);
            wrongMutaionRecordsCopy.removeAll(duplicateRecords);
            duplicateRecords.addAll(wrongMutaionRecordsCopy);
            return duplicateRecords;
        } else if (duplicateRecords.size() > 0) {
            log.info("dup->" + duplicateRecords);
        } else if (wrongMutaionRecords.size() > 0) {
            log.info("wrongMut->" + wrongMutaionRecords);
        }
        return null;
    }

    private List<ErrorRecord> getWrongMutaionRecords(List<Statement> statements) {
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
        ErrorRecord record = new ErrorRecord();
        record.setAccountNumber(statement.getAccountNumber());
        record.setReference(statement.getReference());
        return record;
    }

    private List<ErrorRecord> getDuplicateRecords(List<Statement> jsonData) {
        HashMap<Long, Statement> hMap = new HashMap<>();
        List<ErrorRecord> records = new ArrayList<>();
        for (Statement x : jsonData) {
            Long ref = x.getReference();
            if (hMap.containsKey(ref)) {
                records.add(getErrorRecord(x));
            } else {
                hMap.put(x.getReference(), x);
            }
        }
        return records;
    }


}
