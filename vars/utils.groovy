#!groovy

/*
    author: TQING<tqing1128@gmail.com>
    date: 2023-03-31 17:44:22
*/

def transformMapToString(map) {
    def keyValueDelimiter = map.keyValueDelimiter ? map.keyValueDelimiter : "="
    def elementDelimiter = map.elementDelimiter ? map.elementDelimiter : ","

    if (map.target.isEmpty()) {
        return ''
    } else {
        def ret = ''
        map.target.each { k, v ->
            ret += "${k}${keyValueDelimiter}${v}${elementDelimiter}"
        }
        ret = ret.substring(0, ret.length() - elementDelimiter.length())
        return ret
    }
}

def transformListToString(map) {
    def delimiter = map.delimiter ? map.delimiter : ","

    if (map.target.isEmpty()) {
        return ''
    } else {
        def ret = ''
        map.target.every {
            ret += "${it}${delimiter}"
        }
        ret = ret.substring(0, ret.length() - delimiter.length())
        return ret
    }
}

def transformStringToMap(map) {
    def elementDelimiter = map.elementDelimiter ? map.elementDelimiter : "\n"
    def keyValueDelimiter = map.keyValueDelimiter ? map.keyValueDelimiter : '='

    return map.target.split(elementDelimiter).collectEntries {
        def pair = it.split(keyValueDelimiter)
        [(pair[0].trim()): pair[1].trim()]
    }
}

def getDirList(map) {
    def dir = map.dir ? map.dir : '.'
    def list = sh(returnStdout: true, script: "ls -lt ${dir} | awk '/^d/ {print \$NF}'").trim().split(/\n/)
    def ret = []
    for (int i = 0; i < list.size(); i++) {
        ret.add(dirs[i])
    }
    return ret
}