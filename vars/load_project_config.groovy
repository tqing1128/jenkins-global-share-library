#!groovy

/*
    author: TQING<tqing1128@gmail.com>
    date: 2023-04-11 14:13:44

    用于加载项目配置文件，该文件位于项目根目录下的project_config.groovy
    使用时需要使用 node {} 包裹，否则会报错
*/

def call() {
    return load("${env.JENKINS_PROJECT_CONFIG_DIR}/project_config.groovy")
}