package org.krzysztofk.transfers.accounts;

import java.math.BigDecimal;
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

    public boolean debitAccount(String accountNumber, UUID transferId, BigDecimal amount) {
        return get(accountNumber)
                .map(account -> {
                    if (account.getBalance().compareTo(amount) >= 0) {
                        return accountRepository.update(account, account.debit(transferId, amount));
                    } else {
                        return false;
                    }
                })
                .orElse(false);
    }

    public boolean creditAccount(String number, UUID transferId, BigDecimal amount) {
        return get(number)
                .map(account -> accountRepository.update(account, account.credit(transferId, amount)))
                .orElse(false);
    }

    public boolean cancelDebit(String number, UUID transferId, BigDecimal amount) {
        return get(number)
                .map(account -> accountRepository.update(account, account.cancelDebit(transferId, amount)))
                .orElse(false);
    }
}