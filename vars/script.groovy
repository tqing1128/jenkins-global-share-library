#!groovy

/*
    author: TQING<tqing1128@gmail.com>
    date: 2024-01-16 20:15:29
*/

def readProperties(map) {
    return """
def transformStringToMap(map) {
    def elementDelimiter = map.elementDelimiter ? map.elementDelimiter : "\\n"
    def keyValueDelimiter = map.keyValueDelimiter ? map.keyValueDelimiter : '='

    return map.target.split(elementDelimiter).collectEntries {
        def pair = it.split(keyValueDelimiter)
        [(pair[0].trim()): pair[1].trim()]
    }
}

def read(key) {
    def text = new File("${map.path}").getText("UTF-8")
    def props = transformStringToMap target: text, elementDelimiter: "\\n", keyValueDelimiter: '='
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

try {
    def splitDelimiter = ${map.splitDelimiter} ? ${map.splitDelimiter} : ","
    return read("${map.key}").split(splitDelimiter)
} catch (Exception e) {
    return "\${e}"
}"""
}