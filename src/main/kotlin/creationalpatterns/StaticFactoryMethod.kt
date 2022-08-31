package creationalpatterns

fun main() {
    Server.getServer(8080)
}

class Server private constructor(port: Long) {
    init {
        println("Server started on port $port")
    }
    // We discussed the object keyword earlier in this chapter in the Singleton section. Now, we'll see another use of it as a companion object.
    //In Java, Static Factory Methods are declared static. But in Kotlin, there's no such keyword.
    // Instead, methods that don't belong to an instance of a class can be declared inside companion
    // object:
    companion object {
        fun getServer(port: Long) = Server(port)
    }
    // Companion objects may have a name – for example, companion object parser. But this is only
    // to provide clarity about what the goal of the object is.

    // As you can see, this time, we have declared an object that is prefixed by the companion keyword.
    // Also, it's located inside a class, and not at the package level in the way we saw in the
    // Singleton design pattern



    // Just like a Java static method, calling a companion object will lazily instantiate it when
    // the containing class is accessed for the first time:
    // Server.withPort(8080) // Server started on port 8080
    // Moreover, calling it on an instance of a class simply won't work, unlike in Java:
    // Server(8080) // Won't compile, constructor is private

    // Important Note: A class may have only one companion object.

}