package org.krzysztofk.transfers.web.accounts

import ratpack.test.MainClassApplicationUnderTest

class AccountsRequests {

    static postAccount(MainClassApplicationUnderTest application, String accountJson) {
        application.httpClient.requestSpec { spec ->
            spec.headers.'Content-Type' = ['application/json']
            spec.body { body ->
                body.text(accountJson)
            }
        }.post('/accounts')
    }

    static getAccount(MainClassApplicationUnderTest application, String accountNumber) {
        application.httpClient.get('accounts/' + accountNumber)
    }

}
