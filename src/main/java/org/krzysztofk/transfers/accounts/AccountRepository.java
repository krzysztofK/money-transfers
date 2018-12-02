package org.krzysztofk.transfers.accounts;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class AccountRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    void add(Account account) {
        accounts.put(account.getNumber(), account);
    }

    Optional<Account> get(String number) {
        return Optional.ofNullable(accounts.get(number));
    }
}
