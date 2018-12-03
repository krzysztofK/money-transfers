package org.krzysztofk.transfers.accounts;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class AccountService {

    private final AccountRepository accountRepository = new AccountRepository();

    public Account createAccount(String number, BigDecimal balance) {
        Account account = Account.of(number, balance);
        accountRepository.add(account);
        return account;
    }

    public Optional<Account> get(String number) {
        return accountRepository.get(number);
    }

    public void debitAccount(String number, UUID transferId, BigDecimal amount) {
        Debit debit = new Debit(transferId, amount, Instant.now());
        get(number).map(account -> accountRepository.update(account, account.debit(debit)));
    }
}
