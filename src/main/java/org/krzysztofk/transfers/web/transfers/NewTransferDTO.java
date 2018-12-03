package org.krzysztofk.transfers.web.transfers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

class NewTransferDTO {

    private final String debitedAccountNumber;
    private final String creditedAccountNumber;
    private final BigDecimal amount;

    NewTransferDTO(@JsonProperty("debitedAccountNumber") String debitedAccountNumber,
                   @JsonProperty("creditedAccountNumber") String creditedAccountNumber,
                   @JsonProperty("amount") BigDecimal amount) {
        this.debitedAccountNumber = debitedAccountNumber;
        this.creditedAccountNumber = creditedAccountNumber;
        this.amount = amount;
    }

    public String getDebitedAccountNumber() {
        return debitedAccountNumber;
    }

    public String getCreditedAccountNumber() {
        return creditedAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
