package org.krzysztofk.transfers.web

import groovy.json.JsonSlurper
import ratpack.http.Status
import ratpack.test.embed.EmbeddedApp
import spock.lang.Specification

class AccountsHandlerTest extends Specification {

    def accounts = EmbeddedApp.fromHandler(new AccountsHandler())
    def jsonSlurper = new JsonSlurper()

    def cleanup() {
        accounts.close()
    }

    def 'should add account'() {
        when:
        def response = postAccount(accountJson)

        then:
        response.status == Status.OK
        def parsedResponse = jsonSlurper.parseText(response.body.text)
        parsedResponse.number == '1111494353829482435345'
        parsedResponse.balance == 100.0
    }

    def postAccount(String accountJson) {
        accounts.httpClient.requestSpec { spec ->
            spec.headers.'Content-Type' = ['application/json']
            spec.body { body ->
                body.text(accountJson)
            }
        }.post()
    }

    def accountJson = '''{
      "number": "1111494353829482435345",
      "balance": 100.0
    }'''
}
