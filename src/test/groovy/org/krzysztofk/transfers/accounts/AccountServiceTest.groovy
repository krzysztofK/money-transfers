package org.krzysztofk.transfers.accounts

import spock.lang.Specification

class AccountServiceTest extends Specification {

    def accountService

    def setup() {
        accountService = new AccountService()
    }

    def 'should add account'() {
        given:
        def accountNumber = '11110000'

        when:
        def addedAccount = accountService.createAccount(accountNumber, 100.0)

        then:
        addedAccount.number == accountNumber
        addedAccount.balance == 100.0
    }

    def 'should throw exception if account already exists'() {
        given:
        accountService.createAccount('11110000', 100.0)

        when:
        accountService.createAccount('11110000', 100.0)

        then:
        thrown(AccountAlreadyExistsException)
    }

    def 'should get account by number'() {
        given:
        def accountNumber = '11110000'
        accountService.createAccount('11110000', 100.0)

        when:
        def readAccount = accountService.get(accountNumber)

        then:
        with(readAccount.get()) {
            number == accountNumber
            balance == 100.0
        }
    }

    def 'should debit account'() {
        given:
        def account = accountService.createAccount('11110000', 100.0)

        expect:
        accountService.debitAccount(account.number, UUID.randomUUID(), 10.0)
        accountService.get(account.number).get().balance == 90.0
    }

    def 'should not debit account if not enough money available'() {
        given:
        def account = accountService.createAccount('11110000', 5.0)

        expect:
        !accountService.debitAccount(account.number, UUID.randomUUID(), 10.0)
        accountService.get(account.number).get().balance == 5.0
    }

    def 'should not debit account if no account with requested id'() {
        expect:
        !accountService.debitAccount('11110000', UUID.randomUUID(), 10.0)
    }

}
