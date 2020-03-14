package com.cts.statementprocessor.service;

import com.cts.statementprocessor.beans.Result;
import com.cts.statementprocessor.beans.Statement;
import java.util.List;


public interface StatementService {

    Result parseStatement(List<Statement> jsonData);
}
