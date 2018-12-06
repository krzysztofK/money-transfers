package org.krzysztofk.transfers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
                .registryOf(registry ->
                        registry.add(ObjectMapper.class, configureObjectMapper()))
        );
    }

    private static ObjectMapper configureObjectMapper() {
        return new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}