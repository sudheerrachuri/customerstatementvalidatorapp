package com.cts.statementprocessor.beans;

import lombok.Data;


import java.math.BigDecimal;

@Data
public class Statement {
    private long reference;
    private String accountNumber;
    private String description;
    private BigDecimal startBalance;
    private BigDecimal mutation;
    private BigDecimal endBalance;
}
