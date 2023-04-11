#!groovy

/*
    author: TQING<tqing1128@gmail.com>
    date: 2023-03-31 17:43:48
*/

import groovy.json.JsonSlurperClassic

def getClusterList(host, token) {
    def url = "http://${host}/openapi/v1/apps/pp/envclusters"
    def connection = new URL(url).openConnection();
    connection.setRequestMethod("GET")
    connection.setDoOutput(true)
    connection.setRequestProperty("Content-Type", "application/json")
    connection.setRequestProperty("Authorization", "${token}")
    // connection.getOutputStream().write(jsonContent.getBytes("UTF-8"))
    def status = connection.getResponseCode()
    println("response status: ${status}")
    if(status.equals(200)) {
        def content = connection.getInputStream().getText()
        println("response content: ${content}")
        def ret = new JsonSlurperClassic().parseText(content)
        return ret
    } else {
        return [:]
    }
}