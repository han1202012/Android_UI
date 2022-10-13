class MyPluginExtensions {
    def name
    def age

    // 在扩展中定义方法
    def extensionFun() {
        println 'MyPluginExtensions extensionFun'
    }

    // 在扩展中定义 带参数的方法
    def extensionFun(String str) {
        println 'MyPluginExtensions extensionFun : ' + str
    }
}