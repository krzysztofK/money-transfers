package org.krzysztofk.transfers.accounts;

import java.math.BigDecimal;

public class Account {

    private final String number;
    private final BigDecimal balance;

    Account(String number, BigDecimal balance) {
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
