package creationalpatterns

// The Prototype design pattern is all about customization and creating objects that are similar
// but slightly different. To understand it better, Let's look at an example.

// Imagine we have a system that manages users and their permissions. A data class representing a
// user might look like this:

data class User (
    val name: String,
    val role: Role,
    val permissions: Set<String>,
    val task: String,
)
{
    fun hasPermission(permission: String) = permission in permissions
}

enum class Role {
    ADMIN, SUPER_ADMIN, REGULAR_USER
}

// The enum classes are a way to represent a collection of constants. This is more convenient than
// representing a role as a string, for example, as we check at compile time that such an object exists.

val allUsers = mutableListOf<User>()

fun createUser(name: String, role: Role) {
    for (u in allUsers) {
        if (u.role == role) {
            allUsers += User(name, role, u.permissions, "Do the laundry")
            return
        }
    }
}

// Let's imagine that we now have to add a new field to the User class which we will name tasks:

//data class User (
//    val name: String,
//    val role: Role,
//    val permissions: Set<String>,
//)
//{
//    fun hasPermission(permission: String) = permission in permissions
//}

// Our createUser function will stop compiling . We'll have to change it by copying the value
// of this newly added field to the new instance of our class:

//allUsers += User(name, role, u.permissions, u.tasks)

// This work will have to be repeated every time the User class is changed.

// -------------SOLUTION-------------
// The constant requirement of changes to the code is a clear indication that we need another
// approach to solve this problem.

// THE PROTOTYPE PATTERN

// The whole idea of a prototype is to be able to clone an object easily. There are at least two
// reasons you may want to do this:
// 1. It helps in instances where creating your object is very expensive – for example, if you
// need to fetch it from the database.
// 2. It helps if you need to create objects that are similar but vary slightly and you don't
// want to repeat similar parts over and over again.


fun createUser2(name: String, role: Role) {
    val task = "Wash Dish"
    for (u in allUsers) {
        if (u.role == role) {
            allUsers += u.copy(name=name, task="Wash Dishes") // see the difference between line 31 and this line
            // we are taking the existing values of u object and just changing the name and task
            return
        }
    }
}