package org.krzysztofk.transfers.web.transfers;

import org.krzysztofk.transfers.transfers.Transfer;
import org.krzysztofk.transfers.transfers.TransferService;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.Status;
import ratpack.jackson.Jackson;

import java.util.Optional;
import java.util.UUID;

import static ratpack.jackson.Jackson.fromJson;

public class TransfersHandler implements Handler {

    private final TransferService transferService;

    public TransfersHandler(TransferService transferService) {
        this.transferService = transferService;
    }

    @Override
    public void handle(Context context) throws Exception {
        context.byMethod(method ->
                method
                        .post(() -> {
                            context.getResponse().status(Status.CREATED);
                            context.render(
                                    context.parse(fromJson(NewTransferDTO.class))
                                            .map(this::handleNewTransfer)
                                            .map(Jackson.toJson(context)));
                        })
                        .get(() -> {
                            Optional<Transfer> transfer = transferService.get(UUID.fromString(context.getAllPathTokens().get("id")));
                            if (transfer.isPresent()) {
                                context.render(Jackson.toJson(context).apply(transfer.get()));
                            } else {
                                context.getResponse().status(Status.NOT_FOUND).send();
                            }
                        }));
    }

    private Transfer handleNewTransfer(NewTransferDTO newTransferDTO) {
        return transferService.createTransfer(newTransferDTO.getDebitedAccountNumber(),
                newTransferDTO.getCreditedAccountNumber(),
                newTransferDTO.getAmount());
    }
}
