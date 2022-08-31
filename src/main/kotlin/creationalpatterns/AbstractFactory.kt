package creationalpatterns

import java.lang.RuntimeException

interface Property {
    val name: String
    val value: Any
}

interface ServerConfiguration {
    val properties: List<Property>
}

data class PropertyImpl(
    override val name: String,
    override val value: Any
    ) : Property

// Instead of using a single implementation with a value of Any type(like the data class above)
// ,we'll use two seperate implementations like the 2 data classes below.

data class IntPropertyImpl(
    override val name: String,
    override val value: Int
    ) : Property

data class StringPropertyImpl(
    override val name: String,
    override val value: String
    ) : Property


data class ServerConfigurationImpl(
    override val properties: List<Property>
    ): ServerConfiguration

// factory method for creating Property object
class Parser {
    companion object { // remember there's no static keyword in kotlin. Therefore, this is equivalent to static.
        // It lets us call the methods without instantiating a new object of the class
        fun property(prop: String): Property {
            val (name, value) = prop.split(":")
            return when (name) {
                "port" -> IntPropertyImpl(name, value.trim().toInt())
                "environment" -> StringPropertyImpl(name, value.trim())
                else -> throw RuntimeException("Unknown Property: $name")
            }
        }

        // factory method for creating Property object
        fun server(propertyStrings: List<String>): ServerConfiguration {
            val parsedProperties = mutableListOf<Property>()
            for (p in propertyStrings) {
                parsedProperties += property(p) // using the factory method to create property object
            }
            return ServerConfigurationImpl(parsedProperties)
        }
    }
}

fun main() {
    val portProperty = Parser.property("port:8080")
    val envProperty = Parser.property("environment:qa")
    val port:Int = portProperty.value as Int // this is called unsafe cast
    val port2:Int? = portProperty.value as? Int // this is safe cast

    // Instead of resorting to casts, let's try another approach: Sub-classing!

//    val port3:Int = portProperty.value // gives error so let's use smart casts to solve this
    // issue... we can check if an object is of a given type by using the is keyword
    if(portProperty is IntPropertyImpl)
    {
        val port3: Int = portProperty.value
    }

    // we can test our second factory method works well:
    println(Parser.server(listOf("port:8080", "environment:production")))
}