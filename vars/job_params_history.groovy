#!groovy

/*
    author: TQING<tqing1128@gmail.com>
    date: 2023-03-31 17:44:11
*/

def read() {
    def historyProperties = new File("${env.WORKSPACE}", "history_${env.BUILD_USER_ID}.properties").getText()
    def props = utils.transformStringToMap target: historyProperties, elementDelimiter: "\n", keyValueDelimiter: '='
    return props
}
def read(key) {
    def historyProperties = new File("${env.WORKSPACE}", "history_${env.BUILD_USER_ID}.properties").getText()
    def props = utils.transformStringToMap target: historyProperties, elementDelimiter: "\n", keyValueDelimiter: '='
    if(props.isEmpty()) {
        return ''
    } else {
        def value = props[key]
        if(value) {
            return value
        } else {
            return ''
        }
    }
}

def write(map) {
    if(map.filter) {
        def historyParams = [:]
        map.filter.every {
            if(map.params[it]) {
                historyParams[it] = map.params[it]
            }
        }
        map.params = historyParams
    }

    def historyProperties = utils.transformMapToString target: map.params, keyValueDelimiter: "=", elementDelimiter: "\n"
    println "history: ${historyProperties}"
    new File("${env.WORKSPACE}", "history_${env.BUILD_USER_ID}.properties").write(historyProperties)
}