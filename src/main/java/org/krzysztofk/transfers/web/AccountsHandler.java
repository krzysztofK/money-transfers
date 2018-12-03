package org.krzysztofk.transfers.web;

import org.krzysztofk.transfers.accounts.Account;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.Status;
import ratpack.jackson.Jackson;

import static ratpack.jackson.Jackson.fromJson;

public class AccountsHandler implements Handler {

    @Override
    public void handle(Context context) throws Exception {
        context.byMethod(method ->
                method.post(() -> {
                    context.getResponse().status(Status.CREATED);
                    context.render(
                            context.parse(fromJson(Account.class))
                                    .map(this::handlePostAccount)
                                    .map(Jackson.toJson(context)));
                }));
    }

    private Account handlePostAccount(Account account) {
        System.out.println(account.getNumber() + " " + account.getBalance());
        return account;
    }
}
