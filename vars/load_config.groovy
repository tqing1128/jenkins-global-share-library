#!groovy

/*
    author: TQING<tqing1128@gmail.com>
    date: 2023-04-11 14:13:44
*/

def call() {
    def content = new File("${env.JENKINS_HOME}", "project_config.properties").getText()
    def props = utils.transformStringToMap target:content , elementDelimiter: "\n", keyValueDelimiter: '='
    return props
}