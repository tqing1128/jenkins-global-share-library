#!groovy

/*
    author: TQING<tqing1128@gmail.com>
    date: 2023-03-31 17:43:48
*/

import groovy.json.JsonOutput
import groovy.json.JsonSlurperClassic

private def requestApollo(map) {
    def method = map.method
    def url = map.url
    def token = map.token
    def data = map.data

    println("requestApollo(url: ${url}")
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
        return [true, ret]
    } else {
        return [false, status]
    }
}

def getClusterList(appId, host, token) {
    def url = "http://${host}/openapi/v1/apps/${appId}/envclusters"
    return requestApollo(method: "GET", url: url, token: token)
}

def getCluster(map) {
    def host = map.host
    def appId = map.appId
    def env = map.env
    def cluster = map.cluster
    def token = map.token

    def url = "http://${host}/openapi/v1/envs/${env}/apps/${appId}/clusters/${cluster}"
    return requestApollo(method: "GET", url: url, token: token)
}

def createCluster(map) {
    def host = map.host
    def appId = map.appId
    def env = map.env
    def token = map.token

    def name = map.name
    def user = map.user

    def url = "http://${host}/openapi/v1/envs/${env}/apps/${appId}/clusters"
    def data = [
        name: name,
        appId: appId,
        dataChangeCreatedBy: user
    ]
    return requestApollo(method: "POST", url: url, token: token, data: data)
}

def getAllNamespace(map) {
    def host = map.host
    def appId = map.appId
    def env = map.env
    def token = map.token
    def cluster = map.cluster

    def url = "http://${host}/openapi/v1/envs/${env}/apps/${appId}/clusters/${cluster}/namespaces"
    return requestApollo(method: "GET", url: url, token: token)
}

def getNamespace(map) {
    def host = map.host
    def appId = map.appId
    def env = map.env
    def token = map.token
    def cluster = map.cluster
    def namespace = map.namespace

    def url = "http://${host}/openapi/v1/envs/${env}/apps/${appId}/clusters/${cluster}/namespaces/${namespace}"
    return requestApollo(method: "GET", url: url, token: token)
}

def createNamespace(map) {
    def host = map.host
    def appId = map.appId
    def token = map.token

    def name = map.name
    def format = map.format
    def isPublic = map.isPublic
    def comment = map.comment
    def user = map.user

    def url = "http://${map.host}/openapi/v1/apps/${map.appId}/appnamespaces"

    def data = [
        name: map.name,
        appId: map.appId,
        format: map.format,
        isPublic: map.isPublic,
        comment: map.comment,
        dataChangeCreatedBy: map.user
    ]
    return requestApollo(method: "POST", url: url, token: token, data: data)
}

def getItem(map) {
    def host = map.host
    def appId = map.appId
    def env = map.env
    def token = map.token
    def cluster = map.cluster
    def namespace = map.namespace
    def key = map.key

    def url = "http://${host}/openapi/v1/envs/${env}/apps/${appId}/clusters/${cluster}/namespaces/${namespace}/items/${key}"
    return requestApollo(method: "GET", url: url, token: token)
}

def createItem(map) {
    def host = map.host
    def appId = map.appId
    def env = map.env
    def token = map.token
    def cluster = map.cluster
    def namespace = map.namespace

    def key = map.key
    def value = map.value
    def comment = map.comment
    def user = map.user

    def url = "http://${host}/openapi/v1/envs/${env}/apps/${appId}/clusters/${cluster}/namespaces/${namespace}/items"
    def data = [
        key: key,
        value: value,
        comment: comment,
        dataChangeCreatedBy: user
    ]
    return requestApollo(method: "POST", url: url, token: token, data: data)
}

def updateItem(map) {
    def host = map.host
    def appId = map.appId
    def env = map.env
    def token = map.token
    def cluster = map.cluster
    def namespace = map.namespace

    def key = map.key
    def value = map.value
    def comment = map.comment
    def user = map.user
    def createIfNotExists = map.createIfNotExists or true

    def url = "http://${host}/openapi/v1/envs/${env}/apps/${appId}/clusters/${cluster}/namespaces/${namespace}/items/${key}?createIfNotExists=${createIfNotExists}"

    def data = [
        key: key,
        value: value,
        comment: comment,
        dataChangeLastModifiedBy: user,
        dataChangeCreatedBy: user
    ]
    return requestApollo(method: "PUT", url: url, token: token, data: data)
}

def deleteItem(map) {
    def host = map.host
    def appId = map.appId
    def env = map.env
    def token = map.token
    def cluster = map.cluster
    def namespace = map.namespace
    def key = map.key
    def user = map.user

    def url = "http://${host}/openapi/v1/envs/${env}/apps/${appId}/clusters/${cluster}/namespaces/${namespace}/items/${key}?operator=${user}"
    return requestApollo(method: "DELETE", url: url, token: token)
}

def releaseNamespace(map) {
    def host = map.host
    def appId = map.appId
    def env = map.env
    def token = map.token
    def cluster = map.cluster
    def namespace = map.namespace
    def title = map.title
    def comment = map.comment
    def user = map.user

    def url = "http://${host}/openapi/v1/envs/${env}/apps/${appId}/clusters/${cluster}/namespaces/${namespace}/releases"
    def data = [
        releaseTitle: title,
        releaseComment: comment,
        releasedBy: user
    ]
    return requestApollo(method: "POST", url: url, token: token, data: data)
}