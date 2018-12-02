package org.krzysztofk.transfers.accounts;

import java.util.Optional;

public class AccountService {

    private final AccountRepository accountRepository = new AccountRepository();

    public Account add(Account account) {
        accountRepository.add(account);
        return account;
    }

    public Optional<Account> get(String number) {
        return accountRepository.get(number);
    }
}
