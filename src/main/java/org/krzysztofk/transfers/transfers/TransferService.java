package org.krzysztofk.transfers.transfers;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class TransferService {

    private final TransferRepository transferRepository = new TransferRepository();

    public Transfer createTransfer(String debitedAccountNumber, String creditedAccountNumber, BigDecimal amount) {
        Transfer transfer = new Transfer(randomUUID(), debitedAccountNumber, creditedAccountNumber, amount);
        transferRepository.add(transfer);
        return transfer;
    }

    public Optional<Transfer> get(UUID transferId) {
        return transferRepository.get(transferId);
    }
}
