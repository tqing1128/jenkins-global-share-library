#!groovy

/*
    author: TQING<tqing1128@gmail.com>
    date: 2023-08-22 09:47:11
*/

package org.devops

import groovy.json.JsonOutput
import groovy.json.JsonSlurperClassic

@groovy.transform.Field
def data = [:]

def load(key) {
    def name = "${env.JENKINS_DATA_DIR}/${key}.json"
    sh "touch ${name}"
    def json = readFile name
    if(json) {
        return new JsonSlurperClassic().parseText(json)
    } else {
        return [:]
    }
}

def write(key, value) {
    data[key] = value
    def json = JsonOutput.toJson(value)
    def name = "${env.JENKINS_DATA_DIR}/${key}.json"
    writeFile file:name, text:json
}

def read(key) {
    if(data[key]) {
        return data[key]
    } else {
        def value = load(key)
        data[key] = value
        return value
    }
}