package org.krzysztofk.transfers.accounts;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class AccountRepository {

    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    void add(Account account) {
        if (accounts.putIfAbsent(account.getNumber(), account) != null) {
            throw new AccountAlreadyExistsException();
        }
    }

    Optional<Account> get(String number) {
        return Optional.ofNullable(accounts.get(number));
    }
}
