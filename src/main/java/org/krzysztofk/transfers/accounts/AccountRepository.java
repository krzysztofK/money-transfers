package org.krzysztofk.transfers.accounts;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class AccountRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    void add(Account account) {
        if (accounts.putIfAbsent(account.getNumber(), account) != null) {
            throw new AccountAlreadyExistsException();
        }
    }

    Optional<Account> get(String number) {
        return Optional.ofNullable(accounts.get(number));
    }

    void update(Account oldAccount, Account newAccount) {
        if (!accounts.replace(newAccount.getNumber(), oldAccount, newAccount)) {
            throw new Retrier.ConcurrentUpdateException();
        }
    }
}
