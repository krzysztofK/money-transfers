package org.krzysztofk.transfers.accounts;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Operation {

    private final UUID transferId;
    private final BigDecimal amount;
    private final BigDecimal balance;
    private final OperationType operationType;
    private final Instant operationTime;

    Operation(UUID transferId, BigDecimal amount, BigDecimal balance, OperationType operationType) {
        this.transferId = transferId;
        this.amount = amount;
        this.balance = balance;
        this.operationType = operationType;
        this.operationTime = Instant.now();
    }

    static Operation debit(UUID transferId, BigDecimal amount, BigDecimal balance) {
        return new Operation(transferId, amount, balance, OperationType.DEBIT);
    }

    static Operation credit(UUID transferId, BigDecimal amount, BigDecimal balance) {
        return new Operation(transferId, amount, balance, OperationType.CREDIT);
    }

    static Operation debitCancel(UUID transferId, BigDecimal amount, BigDecimal balance) {
        return new Operation(transferId, amount, balance, OperationType.DEBIT_CANCEL);
    }

    public UUID getTransferId() {
        return transferId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public Instant getOperationTime() {
        return operationTime;
    }
}
