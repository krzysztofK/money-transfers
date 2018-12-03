package org.krzysztofk.transfers;

import org.krzysztofk.transfers.accounts.AccountService;
import org.krzysztofk.transfers.transfers.TransferService;
import org.krzysztofk.transfers.web.accounts.AccountsHandler;
import org.krzysztofk.transfers.web.transfers.TransfersHandler;
import ratpack.server.RatpackServer;

public class Main {
    public static void main(String... args) throws Exception {
        AccountService accountService = new AccountService();
        TransferService transferService = new TransferService(accountService);
        RatpackServer.start(server -> server
                .handlers(chain -> chain
                        .path("accounts/:number?", new AccountsHandler(accountService))
                        .path("transfers/:id?", new TransfersHandler(transferService))
                )
        );
    }
}