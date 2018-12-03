package org.krzysztofk.transfers;

import org.krzysztofk.transfers.accounts.AccountService;
import org.krzysztofk.transfers.web.AccountsHandler;
import ratpack.server.RatpackServer;

public class Main {
    public static void main(String... args) throws Exception {
        RatpackServer.start(server -> server
                .handlers(chain -> chain
                        .path("accounts/:number?", new AccountsHandler(new AccountService()))
                )
        );
    }
}