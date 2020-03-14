package com.cts.statementprocessor.beans;

import lombok.Data;

import java.util.List;

@Data
public class Result {

    private String result;
    List<ErrorRecord> errorRecords;
}
