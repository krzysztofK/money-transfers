package org.krzysztofk.transfers.web.accounts;

import org.krzysztofk.transfers.accounts.Account;
import org.krzysztofk.transfers.accounts.AccountService;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.Status;
import ratpack.jackson.Jackson;

import java.util.Optional;

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
                                    context.parse(fromJson(NewAccountDTO.class))
                                            .map(this::handleNewAccount)
                                            .map(Jackson.toJson(context)));
                        })
                        .get(() -> {
                                    Optional<Account> account = accountService.get(context.getAllPathTokens().get("number"));
                                    if (account.isPresent()) {
                                        context.render(Jackson.toJson(context).apply(account.get()));
                                    } else {
                                        context.getResponse().status(Status.NOT_FOUND).send();
                                    }
                                }
                        ));
    }

    private Account handleNewAccount(NewAccountDTO newAccountDTO) {
        return accountService.createAccount(newAccountDTO.getNumber(), newAccountDTO.getBalance());
    }
}
