package org.krzysztofk.transfers.web.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

class NewAccountDTO {

    private final String number;
    private final BigDecimal balance;

    NewAccountDTO(@JsonProperty("number") String number, @JsonProperty("balance") BigDecimal balance) {
        this.number = number;
        this.balance = balance;
    }

    String getNumber() {
        return number;
    }

    BigDecimal getBalance() {
        return balance;
    }
}
