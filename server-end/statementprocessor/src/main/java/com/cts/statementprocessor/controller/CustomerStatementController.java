package com.cts.statementprocessor.controller;

import com.cts.statementprocessor.model.CustomerStatement;
import com.cts.statementprocessor.model.TransactionResponse;
import com.cts.statementprocessor.service.CustomerStatementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller for prcoessing customer statement of Rabobank
 * @author Rachuri Sudheer
 */
@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class CustomerStatementController {

    private final CustomerStatementService customerStatementService;

    /**
     * This api will validate rabobank satements
     * @param customerStatements rabo bank statements as customer statements
     * @return transaction response
     */
    @PostMapping("/process")
    public ResponseEntity<TransactionResponse> processStatements(@RequestBody List<CustomerStatement> customerStatements) {
        log.info("rabo bank statement validation"+customerStatements);
        TransactionResponse transactionResponse = customerStatementService.parseCustomerStatement(customerStatements);
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }
}
