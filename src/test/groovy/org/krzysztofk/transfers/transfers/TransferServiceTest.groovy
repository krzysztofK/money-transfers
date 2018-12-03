package org.krzysztofk.transfers.transfers

import spock.lang.Specification

class TransferServiceTest extends Specification {

    def transferService = new TransferService()

    def 'should create transfer'() {
        when:
        def transfer = transferService.createTransfer('11110000', '22220000', 10.0)

        then:
        transfer.id != null
        transfer.debitedAccountNumber == '11110000'
        transfer.creditedAccountNumber == '22220000'
        transfer.amount == 10.0
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
