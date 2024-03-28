#!groovy

/*
    author: TQING<tqing1128@gmail.com>
    date: 2023-03-31 17:44:05
*/

import org.devops.DingtalkRobot

@groovy.transform.Field
def robots = []

@groovy.transform.Field
def random = new Random()

@groovy.transform.Field
def markdownTemplateConfig = [
    template_1: [
        start: {
            return """# 构建开始🚀🚀🚀

---

### 构建项目: ${env.JOB_NAME}:${env.BUILD_ID}

### 构建用户: ${env.BUILD_USER_NAME}

### 构建时间: ${env.BUILD_DATE}

### 构建描述:

${env.BUILD_DESCRIPTION}

### 构建日志: [点击查看](${env.BUILD_URL}console)
"""
        },
        success: {
            return """# <font color=green>构建成功🥳🥳🥳</font>

---

### 构建项目: ${env.JOB_NAME}:${env.BUILD_ID}

### 构建用户: ${env.BUILD_USER_NAME}

### 构建时间: ${env.BUILD_DATE}(${env.BUILD_TIME}ms)

### 构建描述:

${env.BUILD_DESCRIPTION}

### 构建日志: [点击查看](${env.BUILD_URL}console)
"""
        },
        failure: {
            return """# <font color=red>构建失败😤😤😤</font>

---

### 构建项目: ${env.JOB_NAME}:${env.BUILD_ID}

### 构建用户: ${env.BUILD_USER_NAME}

### 构建时间: ${env.BUILD_DATE}(${env.BUILD_TIME}ms)

### 构建描述:

${env.BUILD_DESCRIPTION}

### 失败原因:

${env.REASON}

### 构建日志: [点击查看](${env.BUILD_URL}console)
"""
        }
    ]
]

private def selectRobot(name) {
    if(name) {
        for (robot in robots) {
            if(robot.getName() == name) {
                return robot
            }
        }
    } else {
        def index = random.nextInt(robots.size())
        println "select robot index: ${index}"
        return robots[index]
    }
}

private def getDelimiter(msgType) {
    switch(msgType) {
        case "text":
            return "\n"
        case "markdown":
            return "\n\n"
        default:
            return "\n"
    }
}

private def getTemplate(msgType, text) {
    switch(msgType) {
        case "text":
            return null
        case "markdown":
            def template = text.split(/\./)
            def templateName = template[0]
            def templateType = template[1]
            if(markdownTemplateConfig[templateName]) {
                def templateFunc = markdownTemplateConfig[templateName][templateType]
                if(templateFunc) {
                    return templateFunc()
                } else {
                    return null
                }
            } else {
                return null
            }
        default:
            return null
    }
}

private def parseText(msgType, text) {
    if(text.class == ArrayList) {
        def delimiter = getDelimiter(msgType)
        return utils.transformListToString(target: text, delimiter: delimiter)
    } else if(text.class == String) {
        def template = getTemplate(msgType, text)
        if(template) {
            return template
        } else {
            return text
        }
    }
    else {
        throw new Exception("text type error")
    }
}

def init(robotConfigList) {
    for (config in robotConfigList) {
        def robot = new DingtalkRobot(config.name, config.token, config.secret)
        robots.add(robot)
    }
}

def dingText(map) {
    def text = parseText("text", map.text)

    def content = [
        msgtype: "text",
        text: [
            content: text,
        ],
        at:[
            atMobiles: map.atMobiles,
            atUserIds: map.atUserIds,
            isAtAll: map.isAtAll
        ]
    ]

    def robot = selectRobot(map.robot)
    
    println "ding text content:${content}"
    def response = robot.ding(content)
    println "ding text response:${response}"
}

def dingMarkdown(map) {
    def text = parseText("markdown", map.text)

    def content = [
        msgtype: "markdown",
        markdown: [
            title: map.title,
            text: text,
        ],
        at:[
            atMobiles: map.atMobiles,
            atUserIds: map.atUserIds,
            isAtAll: map.isAtAll
        ]
    ]

    def robot = selectRobot(map.robot)

    println "ding markdown content:${content}"
    def response = robot.ding(content)
    println "ding markdown response:${response}"
}