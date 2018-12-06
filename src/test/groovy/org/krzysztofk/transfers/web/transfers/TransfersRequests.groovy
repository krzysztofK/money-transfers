package org.krzysztofk.transfers.web.transfers

import ratpack.test.MainClassApplicationUnderTest

class TransfersRequests {
    static postTransfer(MainClassApplicationUnderTest application, String transferJson) {
        application.httpClient.requestSpec { spec ->
            spec.headers.'Content-Type' = ['application/json']
            spec.body { body ->
                body.text(transferJson)
            }
        }.post('/transfers')
    }

    static getTransfer(MainClassApplicationUnderTest application, transferId) {
        application.httpClient.get('transfers/' + transferId)
    }
}
