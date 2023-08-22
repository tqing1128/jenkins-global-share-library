#!groovy

/*
    author: TQING<tqing1128@gmail.com>
    date: 2023-08-22 09:47:11
*/

package org.devops

import groovy.json.JsonOutput
import groovy.json.JsonSlurperClassic

@groovy.transform.Field
def data = []

def write(key, value) {
    data[key] = value
    def json = JsonOutput.toJson(data)
    writeFile file:"${env.JENKINS_DATA_DIR}/db.json", text:json
}

def read(key) {
    return data[key]
}

def readAll() {
    return data
}