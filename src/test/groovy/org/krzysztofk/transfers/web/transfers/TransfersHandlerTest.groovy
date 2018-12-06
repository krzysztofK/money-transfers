package org.krzysztofk.transfers.web.transfers

import groovy.json.JsonSlurper
import org.krzysztofk.transfers.Main
import ratpack.http.Status
import ratpack.test.MainClassApplicationUnderTest
import spock.lang.Specification

import static java.util.UUID.randomUUID

class TransfersHandlerTest extends Specification {

    def application = new MainClassApplicationUnderTest(Main.class)
    def jsonSlurper = new JsonSlurper()

    def cleanup() {
        application.close()
    }

    def 'should add transfer'() {
        when:
        def response = TransfersRequests.postTransfer(application, transferJson)

        then:
        response.status == Status.CREATED
        def parsedResponse = jsonSlurper.parseText(response.body.text)
        parsedResponse.id != null
        parsedResponse.debitedAccountNumber == '11110000'
        parsedResponse.creditedAccountNumber == '22220000'
        parsedResponse.amount == 10.0
        parsedResponse.status == 'PENDING'
    }

    def 'should return 404 if transfer not found'() {
        expect:
        TransfersRequests.getTransfer(application, randomUUID().toString()).status == Status.NOT_FOUND
    }

    def 'should get transfer'() {
        given:
        def postTransferResponse = TransfersRequests.postTransfer(application, transferJson)
        def transferId = jsonSlurper.parseText(postTransferResponse.body.text).id

        when:
        def response = TransfersRequests.getTransfer(application, transferId)

        then:
        response.status == Status.OK
        def parsedResponse = jsonSlurper.parseText(response.body.text)
        parsedResponse.id == transferId
        parsedResponse.debitedAccountNumber == '11110000'
        parsedResponse.creditedAccountNumber == '22220000'
        parsedResponse.amount == 10.0
        parsedResponse.status == 'DEBIT_DISCARDED'
    }

    def transferJson = """{
      "debitedAccountNumber": "11110000",
      "creditedAccountNumber": "22220000",
      "amount": 10.0
    }"""

}
