package org.krzysztofk.transfers.accounts

import spock.lang.Specification

class AccountServiceTest extends Specification {

    def accountService = new AccountService()

    def 'should add account'() {
        given:
        def account = new Account('11110000', 100.0)

        when:
        def addedAccount = accountService.add(account)

        then:
        addedAccount.number == account.number
    }

    def 'should get account by number'() {
        given:
        def account = new Account('11110000', 100.0)
        accountService.add(account)

        when:
        def readAccount = accountService.get(account.number)

        then:
        with(readAccount.get()) {
            number == account.number
            balance == account.balance
        }
    }
}
