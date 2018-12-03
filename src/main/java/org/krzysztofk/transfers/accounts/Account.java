package org.krzysztofk.transfers.accounts;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class Account {

    private final String number;
    private final BigDecimal balance;
    private final List<Debit> debits;

    private Account(String number, BigDecimal balance, List<Debit> debits) {
        this.number = number;
        this.balance = balance;
        this.debits = debits;
    }

    static Account of(String number, BigDecimal balance) {
        return new Account(number, balance, emptyList());
    }

    public String getNumber() {
        return number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Debit> getDebits() {
        return debits;
    }

    Account debit(Debit debit) {
        return new Account(number, calculateBalance(debit), addDebit(debit));
    }

    private BigDecimal calculateBalance(Debit debit) {
        return balance.subtract(debit.getAmount());
    }

    private List<Debit> addDebit(Debit debit) {
        return Stream.concat(debits.stream(), Stream.of(debit)).collect(toList());
    }
}
