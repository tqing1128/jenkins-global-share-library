#!groovy

/*
    author: TQING<tqing1128@gmail.com>
    date: 2023-03-31 17:44:17

    用于加载基础环境变量，包括：
    1. JENKINS_PROJECT_CONFIG_DIR: 项目配置文件目录
    2. JENKINS_WORKSPACE: Jenkins 工作目录
    3. WORKSPACE: Jenkins Job 工作目录
    4. BUILD_DATE: 构建日期
    5. BUILD_TIMESTAMP: 构建时间戳
    6. BUILD_USER_NAME: 构建用户名称
    7. BUILD_USER_ID: 构建用户 ID
    8. REASON: 失败原因
*/

def call() {
    node {
        env.JENKINS_PROJECT_CONFIG_DIR = "/etc/jenkins_project_config"

        env.JENKINS_WORKSPACE = "${env.JENKINS_HOME}/workspace"
        env.WORKSPACE = "${env.WORKSPACE}"

        def now = new Date()
        env.BUILD_DATE = now.format("yyyy-MM-dd HH:mm:ss")
        env.BUILD_TIMESTAMP = now.time

        def properties = getUserCauseProperties()
        env.BUILD_USER_NAME = properties.BUILD_USER_NAME
        env.BUILD_USER_ID = properties.BUILD_USER_ID

        env.REASON = "无"
    }
}

@NonCPS
def getUpstreamBuild() {
   def build = currentBuild.rawBuild
   def upstreamCause
   while (upstreamCause = build.getCause(hudson.model.Cause$UpstreamCause)) {
       build = upstreamCause.upstreamRun
   }
   return build
}

/**
* Get the properties of the build Cause (the event that triggered the build)
* @param upstream If true (Default) return the cause properties of the last upstream job (If the build was triggered by another job or by a chain of jobs)
* @return Map representing the properties of the user that triggered the current build.
*         Contains keys: USER_NAME, USER_ID
*/
@NonCPS
def getUserCauseProperties(Boolean upstream = true) {
   def build = upstream ? getUpstreamBuild() : currentBuild.rawBuild
   def cause = build.getCause(hudson.model.Cause$UserIdCause)
   if (cause) {
       return ['BUILD_USER_NAME': cause.userName, 'BUILD_USER_ID': cause.userId]
   }
   println "Job was not started by a user, it was ${build.getCauses()[0].shortDescription}"
   return [:]
}