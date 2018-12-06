package org.krzysztofk.transfers.accounts;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class Account {

    private final String number;
    private final BigDecimal balance;
    private final List<Operation> operations;

    private Account(String number, BigDecimal balance, List<Operation> operations) {
        this.number = number;
        this.balance = balance;
        this.operations = operations;
    }

    static Account of(String number, BigDecimal balance) {
        return new Account(number, balance, emptyList());
    }

    Account debit(UUID transferId, BigDecimal amount) {
        BigDecimal balance = calculateBalanceWithDebit(amount);
        return new Account(number,
                balance,
                addOperation(Operation.debit(transferId, amount, balance)));
    }

    private BigDecimal calculateBalanceWithDebit(BigDecimal amount) {
        return balance.subtract(amount);
    }

    private List<Operation> addOperation(Operation operation) {
        return Stream.concat(this.operations.stream(), Stream.of(operation)).collect(toList());
    }

    Account credit(UUID transferId, BigDecimal amount) {
        BigDecimal balance = calculateBalanceWithCredit(amount);
        return new Account(number,
                balance,
                addOperation(Operation.credit(transferId, amount, balance)));
    }

    private BigDecimal calculateBalanceWithCredit(BigDecimal amount) {
        return balance.add(amount);
    }

    Account cancelDebit(UUID transferId, BigDecimal amount) {
        BigDecimal balance = calculateBalanceWithCredit(amount);
        return new Account(number,
                balance,
                addOperation(Operation.debitCancel(transferId, amount, balance)));
    }

    public String getNumber() {
        return number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Operation> getOperations() {
        return operations;
    }
}
