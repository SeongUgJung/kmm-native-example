expect fun logError(message: String)

fun logErrorWrapped(message: String) {
    println(">>>>> logError")
    logError(message)
    println("<<<<< logError")
}