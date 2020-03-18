package com.cts.statementprocessor.sevice;

import com.cts.statementprocessor.model.CustomerStatement;
import com.cts.statementprocessor.model.TransactionResponse;
import com.cts.statementprocessor.service.CustomerStatementService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
public class CustomerStatementServiceTests {

    @Autowired
    private CustomerStatementService service;

    List<CustomerStatement> list  = new ArrayList<>();

    CustomerStatement cs = new CustomerStatement();
    @Test
    public void testStatementWithDuplicateRecords(){
        log.info("inside dupliacte records test ");
        cs.setReference(194261);
        cs.setAccountNumber("NL91RABO0315273637");
        cs.setDescription("Clothes from Jan Bakker");
        cs.setEndBalance(BigDecimal.valueOf(20.23));
        cs.setStartBalance(BigDecimal.valueOf(21.6));
        cs.setMutation(BigDecimal.valueOf(-41.83));
        list.add(cs);
        cs.setReference(183049);
        cs.setAccountNumber("NL69ABNA0433647324");
        cs.setDescription("Clothes for Jan King");
        cs.setEndBalance(BigDecimal.valueOf(131.16));
        cs.setStartBalance(BigDecimal.valueOf(86.66));
        cs.setMutation(BigDecimal.valueOf(+44.5));
        list.add(cs);
        cs.setReference(183049);
        cs.setAccountNumber("NL69ABNA0433647323");
        cs.setDescription("Clothes for Jan King");
        cs.setEndBalance(BigDecimal.valueOf(130.16));
        cs.setStartBalance(BigDecimal.valueOf(85.66));
        cs.setMutation(BigDecimal.valueOf(+44.5));
        list.add(cs);

        try{
            TransactionResponse response = service.parseCustomerStatement(list);
            assertNotNull(response);
            assertEquals("DUPLICATE_REFERENCE",response.getResult());
        }catch(Exception ex){

            assertTrue(false);
        }
    }

    @Test
    public void testStatementWithInvalidTransactionRecords(){
        log.info("inside incorrect balance records test ");

        cs.setReference(194261);
        cs.setAccountNumber("NL91RABO0315273637");
        cs.setDescription("Clothes from Jan Bakker");
        cs.setEndBalance(BigDecimal.valueOf(20.23));
        cs.setStartBalance(BigDecimal.valueOf(21.6));
        cs.setMutation(BigDecimal.valueOf(-42.83));
        list.add(cs);
        try{
            TransactionResponse trResponse = service.parseCustomerStatement(list);
            assertNotNull(trResponse);
            assertEquals("INCORRECT_END_BALANCE",trResponse.getResult());
        }catch(Exception ex){
            assertTrue(false);
        }
    }
    @Test
    public void testStatementWithvalidTransactionRecords(){
        log.info("inside valid records test ");
        try{
            TransactionResponse trResponse = service.parseCustomerStatement(list);
            assertNotNull(trResponse);
            assertEquals("SUCCESS",trResponse.getResult());
        }catch(Exception ex){
            assertTrue(false);
        }
    }
    @Test
    public void testStatementWithBothDuplicateAndInvalidTransactionRecords(){
        log.info("inside both duplicate & incoreect balance records test");
        cs.setReference(194261);
        cs.setAccountNumber("NL91RABO0315273637");
        cs.setDescription("Clothes from Jan Bakker");
        cs.setEndBalance(BigDecimal.valueOf(20.23));
        cs.setStartBalance(BigDecimal.valueOf(21.6));
        cs.setMutation(BigDecimal.valueOf(-42.83));
        list.add(cs);
        cs.setReference(183049);
        cs.setAccountNumber("NL69ABNA0433647324");
        cs.setDescription("Clothes for Jan King");
        cs.setEndBalance(BigDecimal.valueOf(121.16));
        cs.setStartBalance(BigDecimal.valueOf(86.66));
        cs.setMutation(BigDecimal.valueOf(+44.5));
        list.add(cs);
        try{
            TransactionResponse trResponse = service.parseCustomerStatement(list);
            assertNotNull(trResponse);
            assertEquals("DUPLICATE_REFERENCE _INCORRECT_END_BALANCE",trResponse.getResult());
        }catch(Exception ex){
            assertTrue(false);
        }
    }
}
