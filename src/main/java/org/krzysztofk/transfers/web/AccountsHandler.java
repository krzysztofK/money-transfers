package org.krzysztofk.transfers.web;

import org.krzysztofk.transfers.accounts.Account;
import org.krzysztofk.transfers.accounts.AccountService;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.Status;
import ratpack.jackson.Jackson;

import static ratpack.jackson.Jackson.fromJson;

public class AccountsHandler implements Handler {

    private final AccountService accountService;

    public AccountsHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void handle(Context context) throws Exception {
        context.byMethod(method ->
                method
                        .post(() -> {
                            context.getResponse().status(Status.CREATED);
                            context.render(
                                    context.parse(fromJson(Account.class))
                                            .map(accountService::add)
                                            .map(Jackson.toJson(context)));
                        })
                        .get(() ->
                                context.render(
                                        Jackson.toJson(context).apply(
                                                accountService.get(context.getAllPathTokens().get("number")).orElse(null)))));
    }
}
