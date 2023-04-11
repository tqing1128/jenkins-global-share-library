#!groovy

/*
    author: TQING<tqing1128@gmail.com>
    date: 2023-04-11 14:13:44
*/

def call() {
    def config
    node {
        config = load "${env.JENKINS_PROJECT_CONFIG_DIR}/jenkins_project_config.groovy"
    }
    return config
}