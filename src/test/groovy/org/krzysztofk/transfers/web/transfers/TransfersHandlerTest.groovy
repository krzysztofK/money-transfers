package org.krzysztofk.transfers.web.transfers

import groovy.json.JsonSlurper
import org.krzysztofk.transfers.Main
import ratpack.http.Status
import ratpack.test.MainClassApplicationUnderTest
import spock.lang.Specification

class TransfersHandlerTest extends Specification {

    def application = new MainClassApplicationUnderTest(Main.class)
    def jsonSlurper = new JsonSlurper()

    def cleanup() {
        application.close()
    }

    def 'should add transfer'() {
        when:
        def response = postTransfer(transferJson)

        then:
        response.status == Status.CREATED
        def parsedResponse = jsonSlurper.parseText(response.body.text)
        parsedResponse.id != null
        parsedResponse.debitedAccountNumber == '11110000'
        parsedResponse.creditedAccountNumber == '22220000'
        parsedResponse.amount == 10.0
    }

    def 'should get transfer'() {
        given:
        def postTransferResponse = postTransfer(transferJson)
        def transferId = jsonSlurper.parseText(postTransferResponse.body.text).id

        when:
        def response = getTransfer(transferId)

        then:
        response.status == Status.OK
        def parsedResponse = jsonSlurper.parseText(response.body.text)
        parsedResponse.id == transferId
        parsedResponse.debitedAccountNumber == '11110000'
        parsedResponse.creditedAccountNumber == '22220000'
        parsedResponse.amount == 10.0
    }

    def postTransfer(String transferJson) {
        application.httpClient.requestSpec { spec ->
            spec.headers.'Content-Type' = ['application/json']
            spec.body { body ->
                body.text(transferJson)
            }
        }.post('/transfers')
    }

    def getTransfer(transferId) {
        application.httpClient.get('transfers/' + transferId)
    }

    def transferJson = """{
      "debitedAccountNumber": "11110000",
      "creditedAccountNumber": "22220000",
      "amount": 10.0
    }"""

}
