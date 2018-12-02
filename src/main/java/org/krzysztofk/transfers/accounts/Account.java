package org.krzysztofk.transfers.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Account {

    private final String number;
    private final BigDecimal balance;

    public Account(@JsonProperty("number") String number, @JsonProperty("balance") BigDecimal balance) {
        this.number = number;
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
