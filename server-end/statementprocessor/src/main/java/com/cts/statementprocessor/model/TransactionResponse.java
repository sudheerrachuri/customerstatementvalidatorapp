package com.cts.statementprocessor.model;

import lombok.Data;

import java.util.List;

@Data
public class TransactionResponse {

    private String result;
    List<ErrorRecord> errorRecords;
}
