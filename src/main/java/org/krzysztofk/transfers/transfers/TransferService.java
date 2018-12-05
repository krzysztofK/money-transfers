package org.krzysztofk.transfers.transfers;

import org.krzysztofk.transfers.accounts.AccountService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.krzysztofk.transfers.transfers.Transfer.newTransfer;

public class TransferService {

    private final TransferRepository transferRepository = new TransferRepository();
    private final AccountService accountService;

    public TransferService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Transfer createTransfer(String debitedAccountNumber, String creditedAccountNumber, BigDecimal amount) {
        Transfer transfer = newTransfer(debitedAccountNumber, creditedAccountNumber, amount);
        transferRepository.add(transfer);
        executeTransfer(transfer);
        return transfer;
    }

    private void executeTransfer(Transfer transfer) {
        if (accountService.debitAccount(transfer.getDebitedAccountNumber(), transfer.getId(), transfer.getAmount())) {
            if (accountService.creditAccount(transfer.getCreditedAccountNumber(), transfer.getAmount())) {
                setTransferStatus(transfer, Status.COMPLETED);
            } else {
                accountService.cancelDebit(transfer.getDebitedAccountNumber(), transfer.getAmount());
                setTransferStatus(transfer, Status.CREDIT_DISCARDED);
            }
        } else {
            setTransferStatus(transfer, Status.DEBIT_DISCARDED);
        }
    }

    private void setTransferStatus(Transfer transfer, Status status) {
        transferRepository.update(transfer, transfer.withStatus(status));
    }

    public Optional<Transfer> get(UUID transferId) {
        return transferRepository.get(transferId);
    }
}
