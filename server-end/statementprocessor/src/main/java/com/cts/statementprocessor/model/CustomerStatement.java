package com.cts.statementprocessor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


import java.math.BigDecimal;

/**
 * Rabobank customer statement pojo
 * @author Rachuri Sudheer
 */
@Data
public class CustomerStatement{
    @JsonProperty("reference")
    private long reference;
    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("description")
    private String description;
    @JsonProperty("startBalance")
    private BigDecimal startBalance;
    @JsonProperty("mutation")
    private BigDecimal mutation;
    @JsonProperty("endBalance")
    private BigDecimal endBalance;
}
