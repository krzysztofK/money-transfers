package org.krzysztofk.transfers.web;

import ratpack.handling.Context;
import ratpack.handling.Handler;

public class AccountsHandler implements Handler {
    @Override
    public void handle(Context context) throws Exception {
        context.byMethod(method ->
                method.get(() -> context.render("test")));
    }
}
