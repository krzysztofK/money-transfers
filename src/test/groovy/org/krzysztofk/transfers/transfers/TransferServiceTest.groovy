package org.krzysztofk.transfers.transfers

import org.krzysztofk.transfers.accounts.AccountService
import spock.lang.Specification

class TransferServiceTest extends Specification {

    def accountService = new AccountService()
    def transferService = new TransferService(accountService)

    def 'should create transfer'() {
        when:
        def transfer = transferService.createTransfer('11110000', '22220000', 10.0)

        then:
        transfer.id != null
        transfer.debitedAccountNumber == '11110000'
        transfer.creditedAccountNumber == '22220000'
        transfer.amount == 10.0
    }

    def 'should transfer money'() {
        given:
        def accountToDebit = accountService.createAccount('11110000', 100.0)
        def accountToCredit = accountService.createAccount('22220000', 100.0)

        when:
        def transfer = transferService.createTransfer(accountToDebit.number, accountToCredit.number, 10.0)

        then:
        def debitedAccount = accountService.get(accountToDebit.number)
        debitedAccount.get().balance == 90.0
        debitedAccount.get().debits.head().amount == 10.0
        accountService.get(accountToCredit.number).get().balance == 110.0
        transferService.get(transfer.id).get().status == Status.COMPLETED
    }

    def 'should discard transfer if debited account does not have enough money'() {
        given:
        def accountToDebit = accountService.createAccount('11110000', 5.0)
        def accountToCredit = accountService.createAccount('22220000', 100.0)

        when:
        def transfer = transferService.createTransfer(accountToDebit.number, accountToCredit.number, 10.0)

        then:
        accountService.get(accountToDebit.number).get().balance == 5.0
        accountService.get(accountToCredit.number).get().balance == 100.0
        transferService.get(transfer.id).get().status == Status.DEBIT_DISCARDED
    }

    def 'should get transfer by id'() {
        given:
        def createdTransfer = transferService.createTransfer('11110000', '22220000', 10.0)

        when:
        def readTransfer = transferService.get(createdTransfer.id)

        then:
        readTransfer.get().id == createdTransfer.id
        readTransfer.get().debitedAccountNumber == createdTransfer.debitedAccountNumber
        readTransfer.get().creditedAccountNumber == createdTransfer.creditedAccountNumber
        readTransfer.get().amount == createdTransfer.amount
    }
}
