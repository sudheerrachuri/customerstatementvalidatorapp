package com.cts.statementprocessor.beans;

import lombok.Data;

import java.util.List;

@Data
public class ErrorRecord {

    Long reference;
    String accountNumber;

    public ErrorRecord(){};
}
