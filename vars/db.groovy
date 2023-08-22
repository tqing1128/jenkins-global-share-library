#!groovy

/*
    author: TQING<tqing1128@gmail.com>
    date: 2023-08-22 09:47:11
*/

package org.devops

import groovy.json.JsonOutput
import groovy.json.JsonSlurperClassic

@groovy.transform.Field
def data

def initData() {
    if(data) {
        return
    }
    def json = readFile file:"${env.JENKINS_DATA_DIR}/db.json"
    if(json) {
        data = new JsonSlurperClassic().parseText(json)
    } else {
        data = [:]
    }
}

def write(key, value) {
    println "write: ${key}=${value}"
    println "data: ${data}"
    initData()
    data[key] = value
    def json = JsonOutput.toJson(data)
    writeFile file:"${env.JENKINS_DATA_DIR}/db.json", text:json
}

def read(key) {
    initData()
    return data[key] 
}

def readAll() {
    initData()
    return data 
}