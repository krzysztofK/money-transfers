package org.krzysztofk.transfers.web.accounts

import groovy.json.JsonSlurper
import org.krzysztofk.transfers.Main
import ratpack.http.Status
import ratpack.test.MainClassApplicationUnderTest
import spock.lang.Specification

class AccountsHandlerTest extends Specification {

    def application = new MainClassApplicationUnderTest(Main.class)
    def jsonSlurper = new JsonSlurper()

    def cleanup() {
        application.close()
    }

    def 'should add account'() {
        when:
        def response = AccountsRequests.postAccount(application, accountJson)

        then:
        response.status == Status.CREATED
        def parsedResponse = jsonSlurper.parseText(response.body.text)
        parsedResponse.number == '1111494353829482435345'
        parsedResponse.balance == 100.0
    }

    def 'should return 404 if transfer not found'() {
        expect:
        AccountsRequests.getAccount(application, '111122223333').status == Status.NOT_FOUND
    }

    def 'should get account'() {
        given:
        AccountsRequests.postAccount(application, accountJson)

        when:
        def response = AccountsRequests.getAccount(application, accountNumber)

        then:
        response.status == Status.OK
        def parsedResponse = jsonSlurper.parseText(response.body.text)
        parsedResponse.number == '1111494353829482435345'
        parsedResponse.balance == 100.0
    }

    def accountNumber = '1111494353829482435345'
    def accountJson = """{
      "number": "$accountNumber",
      "balance": 100.0
    }"""
}
