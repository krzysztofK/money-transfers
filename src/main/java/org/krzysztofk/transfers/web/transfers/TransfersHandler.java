package org.krzysztofk.transfers.web.transfers;

import org.krzysztofk.transfers.transfers.Transfer;
import org.krzysztofk.transfers.transfers.TransferService;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.Status;
import ratpack.jackson.Jackson;

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
                        }));
    }

    private Transfer handleNewTransfer(NewTransferDTO newTransferDTO) {
        return transferService.createTransfer(newTransferDTO.getDebitedAccountNumber(),
                newTransferDTO.getCreditedAccountNumber(),
                newTransferDTO.getAmount());
    }
}
