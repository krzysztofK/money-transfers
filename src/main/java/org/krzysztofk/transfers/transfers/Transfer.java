package org.krzysztofk.transfers.transfers;

import java.math.BigDecimal;
import java.util.UUID;

public class Transfer {

    private final UUID id;
    private final String debitedAccountNumber;
    private final String creditedAccountNumber;
    private final BigDecimal amount;

    public Transfer(UUID id, String debitedAccountNumber, String creditedAccountNumber, BigDecimal amount) {
        this.id = id;
        this.debitedAccountNumber = debitedAccountNumber;
        this.creditedAccountNumber = creditedAccountNumber;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
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
