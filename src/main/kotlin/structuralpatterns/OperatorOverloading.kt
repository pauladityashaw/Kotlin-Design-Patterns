package structuralpatterns


// OPEREATOR OVERLOADING - not a design pattern

// Most programming languages support some form of operator overloading. Let's take Java as an
// example and look at the following two lines:
// System.out.println(1 + 1); // Prints 2
// System.out.println("1" + "1") // Prints 11
// We can see that the + operator acts differently depending on whether the arguments are strings
// or integers. That is, it can add two numbers, but it can also concatenate two strings.

// You can imagine that the plus operation can be defined on other types. For example, it makes
// a lot of sense to concatenate two lists using the same operator: List.of("a") + List.of("b")

// Unfortunately, this code won't compile in Java, and we can't do anything about it.
// That's because operator overloading is a feature reserved to the language itself,
// and not for its users.



// Let's look at another extreme, the Scala programming language. In Scala, any set of characters
// can be defined as an operator. So, you may encounter code such as the following:
// Seq("a") ==== Seq("b") // You'll have to guess what this code does

// Kotlin takes a middle ground between these two approaches. It allows you to overload certain
// well-known operations, but it limits what can and cannot be overloaded.
// Although this list is limited, it is quite long, so we'll not write it in full here.
// However, you can find it in the official Kotlin documentation:
// https://kotlinlang.org/docs/operator-overloading.html.




// If you use the operator keyword with a function that is unsupported or with the wrong set of
// arguments, you'll get a compilation error. The square brackets that we started with in the
// previous code example are called indexed access operators and correlate with the get(x) and
// set(x, y) methods we have just defined.


//below code is taken from the decorator pattern example
interface StarTechRepostory {
    operator fun get(starshipName: String): String
    operator fun set(starshipName: String, captainName: String)
}

class DefaultStarTechRepository: StarTechRepostory {
    private val starShipCaptains = mutableMapOf("USS Enterprise" to "Jean-Luc Picard")
    override fun get(starshipName: String): String {
        return starShipCaptains[starshipName]?: "Unknown Ship :/"
    }

    override fun set(starshipName: String, captainName: String) {
        starShipCaptains[starshipName] = captainName
    }
}
// https://kotlinlang.org/docs/operator-overloading.html

fun main() {
    val starTrekRepo = DefaultStarTechRepository()
    // this is the magic of operator overloading :
    println(starTrekRepo["USS Enterprise"])
    starTrekRepo["USS Voyager"] = "Kathryn"
}