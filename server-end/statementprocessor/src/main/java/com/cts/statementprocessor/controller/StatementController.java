package com.cts.statementprocessor.controller;

import com.cts.statementprocessor.beans.Result;
import com.cts.statementprocessor.beans.Statement;
import com.cts.statementprocessor.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
public class StatementController {

    private final StatementService statementService;

    @PostMapping("/process")
    public ResponseEntity<Result> processJsonData(@RequestBody List<Statement> jsonData){
        Result jsonResults = statementService.parseStatement(jsonData);

        return new ResponseEntity<>(jsonResults, HttpStatus.OK);
    }
}
