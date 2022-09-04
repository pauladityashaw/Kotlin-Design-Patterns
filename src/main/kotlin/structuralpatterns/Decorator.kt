package structuralpatterns

import java.lang.RuntimeException

// In the previous chapter, we discussed the Prototype design pattern, which allows us to create
// instances of classes with slightly (or not so slightly) different data.
// This raises a question: What if we want to create a set of classes that all have slightly
// different behavior?
//
// Well, since functions in Kotlin are first-class citizens (which we will explain in this
// chapter), you could use the Prototype design pattern to achieve this aim.
// After all, creating a set of classes with slightly different behavior is what JavaScript does
// successfully.
//
// But the goal of this chapter is to discuss another approach to the same problem.
// After all, design patterns are all about approaches.
//
// By implementing the Decorator design pattern, we allow the users of our code to specify the
// abilities they want to add.


// ENHANCING A CLASS

// Let's say that we have a rather simple class that registers all the captains in the Star Trek
// universe along with their vessels:

open class StarTrekRepo {
    private val starshipCaptains = mutableMapOf("USS Enterprise" to "Jean-Luc Picard")

    open fun getCaptain(starshipName: String): String {
        return starshipCaptains[starshipName]?: "Unknown" // ?: is called the Elvis Operator
        // the goal of the Elvis Operator is to provide a default value in case we receive a null value
    }
    open fun addCaptain(starshipName: String, captainName: String) {
        starshipCaptains[starshipName] = captainName
    }
}

// One day, your captain —sorry, scrum master— comes to you with an urgent requirement. From now on,
// every time someone searches for a captain, we must also log this into a console.
// However, there's a catch to this simple task: you cannot modify the StarTrekRepository class
// directly.
// There are other consumers for this class, and they don't need this logging behavior.

// THE INHERITANCE PROBLEM

// Since our class and it's methods are declared open, we can extend the class and override the function
// we need:

class LoggingGetCaptainStarTrekRepository: StarTrekRepo() {
    override fun getCaptain(starshipName: String): String {
        println("Getting captain for $starshipName")
        return super.getCaptain(starshipName)
    }
}

// That was easy ! However, the next day, your boss (sorry, scrum-master) comes again and asks for
// another feature. When adding a captain, we need to check that their name is no longer than
// 15 characters. That may be a problem for some Klingons, but you decide to implement it anyway.
// And, by the way, this feature should not be related to the logging feature we developed
// previously. Sometimes we just want the logging, and sometimes we just want the validation.
// So, here's what our new class will look like:

class ValidatingAddCaptainStarTrekRepository: StarTrekRepo() {
    override fun addCaptain(starshipName: String, captainName: String) {
        if(captainName.length > 15) {
            throw RuntimeException("$captainName is longer than 15 characters :/")
        }
        super.addCaptain(starshipName, captainName)
    }
}

// Another task done.

// However, the next day, another requirement arises: in some cases, we need
// StarTrekRepository to have logging enabled and also perform validation at the same time.
// I guess we'll have to name it LoggingGetCaptainValidatingAddCaptainStarTrekRepository now.

// Problems like this are surprisingly common, and they are a clear indication that a design
// pattern may help us here.

// THE DECORATOR PATTERN

// The purpose of the Decorator design pattern is to add new behaviors to our objects dynamically.
// In our example, logging and validating are two behaviors that we sometimes want to be applied
// to our object and sometimes don't want to be applied.

// We'll start by converting our StarTrekRepo into an interface
interface StarTrekRepostory {
    fun getCaptain(starshipName: String): String
    fun addCaptain(starshipName: String, captainName: String)
}

class DefaultStarTrekRepository: StarTrekRepostory {
    private val starShipCaptains = mutableMapOf("USS Enterprise" to "Jean-Luc Picard")
    override fun getCaptain(starshipName: String): String {
        return starShipCaptains[starshipName]?: "Unknown Ship :/"
    }

    override fun addCaptain(starshipName: String, captainName: String) {
        starShipCaptains[starshipName] = captainName
    }
}

// Next, instead of extending our concrete implementation, we'll implement the interface
// and use a new keyword called by:

class LoggingGetCaptain(private val repository: StarTrekRepostory): StarTrekRepostory by repository {
    override fun getCaptain(starshipName: String): String {
        println("Getting captain for $starshipName")
        return repository.getCaptain(starshipName)
    }
}

// The by keyword delegates the implementation of an interface to another object. That's why the
// LoggingGetCaptain class doesn't have to implement any of the functions declared in the interface.
// They are all implemented by default by another object that the instance wraps.

class ValidatingAdd(private val repository: StarTrekRepostory): StarTrekRepostory by repository {
    private val maxNameLength = 15
    override fun addCaptain(starshipName: String, captainName: String) {
        require(captainName.length < maxNameLength) { // will throw IllegalArgumentException if the expression is false
            println("$captainName is longer than 15 characters")
        }
        repository.addCaptain(starshipName, captainName)
    }
}

// Let's see how it works now:

fun main() {
    val starTrekRepository = DefaultStarTrekRepository()
    val withValidating = ValidatingAdd(starTrekRepository)
    val withLoggingAndValidating = LoggingGetCaptain(withValidating)
    println(withLoggingAndValidating.getCaptain("USS Enterprise"))
    withLoggingAndValidating.addCaptain("Nimbus", "Harry Potter")
    withLoggingAndValidating.addCaptain("USS Voyager", "Kathryn Janeway")
}

// As you can see, this pattern allows us to compose behavior, just as we wanted. Now, let's take
// a short detour and discuss operator overloading in Kotlin, as this will help us to improve our
// design pattern even more.
