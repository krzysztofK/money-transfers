package org.krzysztofk.transfers.transfers;

import org.krzysztofk.transfers.accounts.AccountService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class TransferService {

    private final TransferRepository transferRepository = new TransferRepository();
    private final AccountService accountService;

    public TransferService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Transfer createTransfer(String debitedAccountNumber, String creditedAccountNumber, BigDecimal amount) {
        Transfer transfer = new Transfer(randomUUID(), debitedAccountNumber, creditedAccountNumber, amount);
        transferRepository.add(transfer);
        accountService.debitAccount(debitedAccountNumber, transfer.getId(), amount);
        accountService.creditAccount(creditedAccountNumber, amount);
        return transfer;
    }

    public Optional<Transfer> get(UUID transferId) {
        return transferRepository.get(transferId);
    }
}
