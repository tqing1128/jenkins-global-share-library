/*
    author: TQING<tqing1128@gmail.com>
    date: 2023-03-31 17:43:24
*/

package org.devops

import groovy.json.JsonOutput
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Base64
import java.net.URLEncoder

class DingtalkRobot {
    final String name
    final private String token
    final private String secret

    DingtalkRobot(name, token, secret) {
        this.name = name
        this.token = token
        this.secret = secret
    }

    private def createSign(secret, timestamp) {
        String stringToSign = timestamp + "\n" + secret
        Mac mac = Mac.getInstance("HmacSHA256")
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"))
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"))
        return URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8")
    }

    private def createUrl() {
        def url = "https://oapi.dingtalk.com/robot/send?access_token=" + this.token
        if(this.secret) {
            def timestamp = System.currentTimeMillis()
            def sign = this.createSign(this.secret, timestamp)
            url += "&timestamp=${timestamp}&sign=${sign}"
        }
        return url
    }

    def ding(content) {
        def url = this.createUrl()
        // def jsonContent = writeJSON returnText: true, json: content
        def jsonContent = JsonOutput.toJson(content)
        // def response = httpRequest acceptType: 'APPLICATION_JSON_UTF8', contentType: 'APPLICATION_JSON_UTF8',
                    // httpMode: 'POST', requestBody: jsonContent, url: url

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
}
