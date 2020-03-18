package com.cts.statementprocessor.service;

import com.cts.statementprocessor.model.CustomerStatement;
import com.cts.statementprocessor.model.TransactionResponse;

import java.util.List;


public interface CustomerStatementService {
    TransactionResponse processCustomerStatement(List<CustomerStatement> customerStatements);
}
