package com.cts.statementprocessor.model;

import lombok.Data;

@Data
public class ErrorRecord {

    Long reference;
    String accountNumber;

    public ErrorRecord(){};
}
