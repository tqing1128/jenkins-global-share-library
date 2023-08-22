#!groovy

/*
    author: TQING<tqing1128@gmail.com>
    date: 2023-08-22 10:48:46
*/

import groovy.json.JsonOutput
import java.security.MessageDigest

def clear(appId, appKey, host) {
    // 去除 host 尾部的 /
    if(host.endsWith('/')) {
        host = host.substring(0, host.length() - 1)
    }
    def content = [
        appid: appId,
        reqdata: [],
        timestamp: System.currentTimeMillis(),
    ]
    def signContent = JsonOutput.toJson(content)
    signContent = "${signContent}${appKey}"
    def sign = MessageDigest.getInstance("MD5").digest(signContent.getBytes()).encodeHex().toString()

    content.sign = sign
    def jsonContent = JsonOutput.toJson(content)

    def url = "${host}/im/clear_data"
    def connection = new URL(url).openConnection();
    connection.setRequestMethod("POST")
    connection.setDoOutput(true)
    connection.setRequestProperty("Content-Type", "application/json")
    connection.getOutputStream().write(jsonContent.getBytes("UTF-8"))
    return [
        status:connection.getResponseCode(),
        result:connection.getInputStream().getText()
    ]
}