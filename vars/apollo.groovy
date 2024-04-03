#!groovy

/*
    author: TQING<tqing1128@gmail.com>
    date: 2023-03-31 17:43:48
*/

import groovy.json.JsonOutput
import groovy.json.JsonSlurperClassic

private def requestApollo(method, url, token, data) {
    println("requestApollo url: ${url}")
    def connection = new URL(url).openConnection();
    connection.setRequestMethod(method)
    connection.setDoOutput(true)
    connection.setRequestProperty("Content-Type", "application/json")
    connection.setRequestProperty("Authorization", "${token}")
    
    if (data) {
        def jsonData = JsonOutput().toJson(data)
        connection.getOutputStream().write(jsonData.getBytes("UTF-8"))
    }

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

def getClusterList(appId, host, token) {
    def url = "http://${host}/openapi/v1/apps/${appId}/envclusters"
    def ret = requestApollo("GET", url, token)
    return ret
}

def getNamespace(map) {
    def host = map.host
    def appId = map.appId
    def env = map.env
    def token = map.token
    def cluster = map.cluster
    def namespace = map.namespace

    def url = "http://${host}/openapi/v1/envs/${env}/apps/${appId}/clusters/${cluster}/namespaces/${namespace}"
    return requestApollo("GET", url, token)
}
