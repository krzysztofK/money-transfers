package org.krzysztofk.transfers.web

import groovy.json.JsonSlurper
import org.krzysztofk.transfers.Main
import org.krzysztofk.transfers.web.accounts.AccountsRequests
import org.krzysztofk.transfers.web.transfers.TransfersRequests
import ratpack.test.MainClassApplicationUnderTest
import spock.lang.Specification

class TransfersIntegrationTest extends Specification {

    def application = new MainClassApplicationUnderTest(Main.class)
    def jsonSlurper = new JsonSlurper()

    def cleanup() {
        application.close()
    }

    def 'should add transfer and change accounts'() {
        given:
        AccountsRequests.postAccount(application, accountJson('1111'))
        AccountsRequests.postAccount(application, accountJson('2222'))

        when:
        def postTransferResponse = TransfersRequests.postTransfer(application, transferJson('1111', '2222'))
        def transferId = jsonSlurper.parseText(postTransferResponse.body.text).id

        then:
        def transfer = getParsedTransfer(transferId)
        def account1111 = getParsedAccount('1111')
        def account2222 = getParsedAccount('2222')

        transfer.status == 'COMPLETED'

        account1111.balance == 90.0
        account1111.operations[0].transferId == transferId
        account1111.operations[0].amount == 10.0
        account1111.operations[0].balance == 90.0
        account1111.operations[0].operationType == 'DEBIT'

        account2222.balance == 110.0
        account2222.operations[0].transferId == transferId
        account2222.operations[0].amount == 10.0
        account2222.operations[0].balance == 110.0
        account2222.operations[0].operationType == 'CREDIT'
    }

    def accountJson(String accountNumber) {
        """{
            "number": "$accountNumber",
            "balance": 100.0
        }"""
    }

    def transferJson(String from, String to) {
        """{
                "debitedAccountNumber": "$from",
                "creditedAccountNumber": "$to",
                "amount": 10.0
            }"""
    }

    def getParsedTransfer(id) {
        jsonSlurper.parseText(TransfersRequests.getTransfer(application, id).body.text)
    }

    def getParsedAccount(String number) {
        jsonSlurper.parseText(AccountsRequests.getAccount(application, number).body.text)
    }
}
