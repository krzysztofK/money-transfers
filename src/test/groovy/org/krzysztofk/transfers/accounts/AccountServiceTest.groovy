package org.krzysztofk.transfers.accounts

import spock.lang.Specification

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

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
        addedAccount.operations.empty
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
            operations.empty
        }
    }

    def 'should debit account'() {
        given:
        def account = accountService.createAccount('11110000', 100.0)
        def transferId = UUID.randomUUID()

        expect:
        accountService.debitAccount(account.number, transferId, 10.0)
        with(accountService.get(account.number).get()) {
            balance == 90.0
            operations[0].transferId == transferId
            operations[0].amount == 10.0
            operations[0].balance == 90.0
            operations[0].operationType == OperationType.DEBIT
        }
    }

    def 'should not debit account if not enough money available'() {
        given:
        def account = accountService.createAccount('11110000', 5.0)

        expect:
        !accountService.debitAccount(account.number, UUID.randomUUID(), 10.0)
        with(accountService.get(account.number).get()) {
            balance == 5.0
            operations.empty
        }
    }

    def 'should not debit account if no account with requested number'() {
        expect:
        !accountService.debitAccount('11110000', UUID.randomUUID(), 10.0)
    }

    def 'should accept concurrent account debits'() {
        given:
        def numberOfThreads = 10
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads)
        def account = accountService.createAccount('11110000', 100.0)

        when:
        List<Callable<Boolean>> debitCalls = (1..numberOfThreads).collect {
            debitCall(account.number, 10.0)
        }
        List<Future<Boolean>> results = executorService.invokeAll(debitCalls)

        then:
        results.every { it.get() }
        with(accountService.get(account.number).get()) {
            balance == 0.0
            operations.size() == numberOfThreads
        }
    }

    def 'should accept concurrent account debits while enough money available'() {
        given:
        def numberOfThreads = 10
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads)
        def account = accountService.createAccount('11110000', 55.0)

        when:
        List<Callable<Boolean>> debitCalls = (1..numberOfThreads).collect {
            debitCall(account.number, 10.0)
        }
        List<Future<Boolean>> results = executorService.invokeAll(debitCalls)

        then:
        def acceptedDebits = 0
        def declinedDebits = 0
        results.each { it.get() ? acceptedDebits++ : declinedDebits++ }
        acceptedDebits == 5
        declinedDebits == numberOfThreads - 5
        with(accountService.get(account.number).get()) {
            balance == 5.0
            operations.size() == acceptedDebits
        }
    }

    def 'should not credit account if no account with requested number'() {
        expect:
        !accountService.debitAccount('11110000', UUID.randomUUID(), 10.0)
    }

    def 'should credit account'() {
        given:
        def account = accountService.createAccount('11110000', 100.0)
        def transferId = UUID.randomUUID()

        expect:
        accountService.creditAccount(account.number, transferId, 10.0)
        with(accountService.get(account.number).get()) {
            balance == 110.0
            operations[0].transferId == transferId
            operations[0].amount == 10.0
            operations[0].balance == 110.0
            operations[0].operationType == OperationType.CREDIT
        }
    }

    def 'should accept concurrent account credits'() {
        given:
        def numberOfThreads = 10
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads)
        def account = accountService.createAccount('11110000', 100.0)

        when:
        List<Callable<Boolean>> creditCalls = (1..numberOfThreads).collect {
            creditCall(account.number, 10.0)
        }
        List<Future<Boolean>> results = executorService.invokeAll(creditCalls)

        then:
        results.every { it.get() }
        with(accountService.get(account.number).get()) {
            balance == 200.0
            operations.size() == numberOfThreads
        }
    }

    def 'should cancel debit'() {
        given:
        def account = accountService.createAccount('11110000', 100.0)
        def transferId = UUID.randomUUID()
        accountService.debitAccount(account.number, transferId, 10.0)

        when:
        accountService.cancelDebit(account.number, transferId, 10.0)

        then:
        with(accountService.get(account.number).get()) {
            balance == 100.0
            operations.size == 2
            operations[1].transferId == transferId
            operations[1].amount == 10.0
            operations[1].balance == 100.0
            operations[1].operationType == OperationType.DEBIT_CANCEL
        }
    }

    def 'should accept concurrent account debit cancels'() {
        given:
        def numberOfThreads = 10
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads)
        def account = accountService.createAccount('11110000', 100.0)

        when:
        List<Callable<Boolean>> debitCancelCalls = (1..numberOfThreads).collect {
            debitCancelCall(account.number, 10.0)
        }
        List<Future<Boolean>> results = executorService.invokeAll(debitCancelCalls)

        then:
        results.every { it.get() }
        with(accountService.get(account.number).get()) {
            balance == 200.0
            operations.size() == numberOfThreads
        }
    }

    def debitCall(String accountNumber, BigDecimal amount) {
        [
                call: {
                    accountService.debitAccount(accountNumber, UUID.randomUUID(), amount)
                }
        ] as Callable<Boolean>
    }

    def creditCall(String accountNumber, BigDecimal amount) {
        [
                call: {
                    accountService.creditAccount(accountNumber, UUID.randomUUID(), amount)
                }
        ] as Callable<Boolean>
    }

    def debitCancelCall(String accountNumber, BigDecimal amount) {
        [
                call: {
                    accountService.cancelDebit(accountNumber, UUID.randomUUID(), amount)
                }
        ] as Callable<Boolean>
    }

}
