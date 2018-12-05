package org.krzysztofk.transfers.transfers;

import java.math.BigDecimal;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class Transfer {

    private final UUID id;
    private final String debitedAccountNumber;
    private final String creditedAccountNumber;
    private final BigDecimal amount;
    private final Status status;

    private Transfer(UUID id, String debitedAccountNumber, String creditedAccountNumber, BigDecimal amount, Status status) {
        this.id = id;
        this.debitedAccountNumber = debitedAccountNumber;
        this.creditedAccountNumber = creditedAccountNumber;
        this.amount = amount;
        this.status = status;
    }

    static Transfer newTransfer(String debitedAccountNumber, String creditedAccountNumber, BigDecimal amount) {
        return new Transfer(randomUUID(), debitedAccountNumber, creditedAccountNumber, amount, Status.PENDING);
    }

    Transfer withStatus(Status status) {
        return new Transfer(this.id,
                this.debitedAccountNumber,
                this.creditedAccountNumber,
                this.amount,
                status);
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

    public Status getStatus() {
        return status;
    }
}
