package creationalpatterns

import java.lang.RuntimeException

data class Mail_V1(
    val to: List<String>,
    val cc: List<String>?,
    val title: String?,
    val message: String?,
    val important: Boolean,
)

val mail = Mail_V1(
    listOf("manager@company.com"), // To
    null, // CC
    "Ping ", // Title
    null, // Message
    true, // Important
)

// Since our constructor receives a lot of arguments, we had to put in some comments in
// order not to get confused. But what happens if we need to change this class now? First,
// our code will stop compiling. Second, we need to keep track of the comments.
// In short, constructors with a long list of arguments quickly become a mess.
// This is the problem the Builder design pattern sets out to solve. It decouples the
// assigning of arguments from the creation of objects and allows the creation of complex
// objects one step at a time. In this section, we'll see a number of approaches to this problem.


// APPROACH #3:

class MailBuilder(
    private var to: List<String> = listOf(),
    private var cc: List<String> = listOf(),
    private var title: String = "",
    private var message: String = "",
    private var important: Boolean = false,
    ) {

    class Mail internal constructor(
        val to: List<String>,
        val cc: List<String>,
        val title: String?,
        val message: String?,
        val important: Boolean
    )
    // Note that the constructor is marked using the internal
    // visibility modifier. This means that our Mail class will be accessible to any code
    // inside our module(meaning this file maybe or maybe just visible inside this class).

    fun build(): Mail {
        if(to.isEmpty()) {
            throw RuntimeException("To property is empty")
        }
        return Mail(to, cc, title, message, important)
    }
    fun message(message: String): MailBuilder {
        this.message = message
        return this
    }
    fun to(to: List<String>): MailBuilder {
        this.to = to
        return this
    }
    fun cc(cc: List<String>): MailBuilder {
        this.cc = cc
        return this
    }
    fun title(title: String): MailBuilder {
        this.title = title
        return this
    }
    fun important(important: Boolean): MailBuilder {
        this.important = important
        return this
    }
}

// This is a working approach. But it has a couple of downsides: The properties of our resulting
// class must be repeated insider the builder. For every property, we need to declare a function
// to set its value. Kotlin provides two other ways that you may find even more useful.



// APPROACH#2: Fluent Setters

data class Mail_V2(
    val to: List<String>,
    private var _message: String? = null, // https://www.baeldung.com/kotlin/default-nulls-required-params
    private var _cc: List<String>? = null,
    private var _title: String? = null,
    private var _important: Boolean? = null,
) {
    fun message(message: String) = apply {
        _message = message
    }
    // apply is one of the extension function that every object in Kotlin has
    fun cc(cc: List<String>) = apply {
        _cc = cc
    }
    fun title(title: String) = apply {
        _title = title
    }
    fun important(important: Boolean) = apply {
        _important = important
    }
    //Using underscores for private variables is a common convention in Kotlin. It allows us to
    // avoid repeating this.message = message and mistakes such as message = message.

//    The apply function returns the reference to an object after executing the block. So, it's a
//    shorter version of the setter function from the previous example:
//    fun message(message: String): MailBuilder {
//      this.message = message
//      return this
//    }


}

// This is a nice approach, and it requires less code to implement. However, there are a few
// downsides to this approach too: We had to make all the optional arguments mutable.
// Immutable fields should always be preferred to mutable ones, as they are thread-safe and
// easier to reason about. All of our optional arguments are also nullable. Kotlin is a null-safe
// language, so every time we access them, we first have to check that their value was set.



// APPROACH #3: Default Arguments
data class Mail_V3(
    val to: List<String>,
    val cc: List<String> = listOf(),
    val title: String = "",
    val message: String = "",
    val important: Boolean = false,
)

fun main() {
    val hello = "hello"

    val mailV1 = MailBuilder(listOf("hello@hello.com")).title("What's up?").build()
    println(mailV1)
    var mailV2 = Mail_V2(listOf("manager@company.com")).message("Ping")
    println(mailV2)
    mailV2 = Mail_V2(listOf("manager@company.com", "pauladityashaw@gmail.com")).apply {
        message("Something")
        title("Apply")
    }
    println(mailV2)
    val mailV3 = Mail_V3(listOf("manager@company.com"), listOf(), "Ping")

    // However, note that we had to specify that we don't want anyone in the CC field by providing
    // an empty list, which is a bit inconvenient. What if we wanted to send an email that is only
    // flagged as important? Not having to specify order with fluent setters was very handy.
    // Kotlin has named arguments for that:

    val mailV3P2 = Mail_V3(to= listOf(), important = true)
    println(mailV3P2)

    // CONCLUSION: Combining default parameters with named arguments makes creating complex objects
    // in Kotlin rather easy. For that reason, you will rarely need the Builder design pattern
    // at all in Kotlin.

}
