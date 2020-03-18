package com.cts.statementprocessor.controller;

import com.cts.statementprocessor.beans.CustomerStatement;
import com.cts.statementprocessor.beans.TransactionResponse;
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
     * @param statements rabo bank statements as MultiPartFile
     * @return
     */
    @PostMapping("/process")
    public ResponseEntity<TransactionResponse> processStatements(@RequestBody List<CustomerStatement> statements) {
        log.info("rabo bank statement validation");
        TransactionResponse jsonResults = customerStatementService.parseCustomerStatement(statements);

        return new ResponseEntity<>(jsonResults, HttpStatus.OK);
    }
}
