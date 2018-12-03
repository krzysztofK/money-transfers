package org.krzysztofk.transfers.accounts;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Debit {
    private final UUID transferId;
    private final BigDecimal amount;
    private final Instant debitTime;

    public Debit(UUID transferId, BigDecimal amount, Instant debitTime) {
        this.transferId = transferId;
        this.amount = amount;
        this.debitTime = debitTime;
    }

    public UUID getTransferId() {
        return transferId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getDebitTime() {
        return debitTime;
    }
}
