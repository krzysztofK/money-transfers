package org.krzysztofk.transfers.transfers;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class TransferRepository {

    private final Map<UUID, Transfer> transfers = new ConcurrentHashMap<>();

    void add(Transfer transfer) {
        if (transfers.putIfAbsent(transfer.getId(), transfer) != null) {
            throw new TransferAlreadyExistsException();
        }
    }

    void update(Transfer oldTransfer, Transfer newTransfer) {
        transfers.replace(newTransfer.getId(), oldTransfer, newTransfer);
    }

    Optional<Transfer> get(UUID transferId) {
        return Optional.ofNullable(transfers.get(transferId));
    }
}
