package com.cts.statementprocessor.service;

import com.cts.statementprocessor.beans.CustomerStatement;
import com.cts.statementprocessor.beans.TransactionResponse;

import java.util.List;


public interface CustomerStatementService {
    TransactionResponse parseCustomerStatement(List<CustomerStatement> statements);
}
