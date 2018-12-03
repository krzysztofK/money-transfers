package org.krzysztofk.transfers.accounts;

import java.math.BigDecimal;
import java.util.Optional;

public class AccountService {

    private final AccountRepository accountRepository = new AccountRepository();

    public Account createAccount(String number, BigDecimal balance) {
        Account account = new Account(number, balance);
        accountRepository.add(account);
        return account;
    }

    public Optional<Account> get(String number) {
        return accountRepository.get(number);
    }

    public void debitAccount(String number, BigDecimal amount) {
        get(number).map(account -> accountRepository.update(account, account.debit(amount)));
    }
}
