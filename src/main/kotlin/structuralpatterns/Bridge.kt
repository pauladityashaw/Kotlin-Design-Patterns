package structuralpatterns


// While the Adapter design pattern helps you to work with legacy code, the Bridge design
// pattern helps you to avoid abusing inheritance. The way it works is actually very simple.

// Let's imagine we want to build a system to manage different kinds of troopers for the Galactic Empire.
interface Trooper {
    fun move(x: Long, y: Long)
    fun attackRebel(x: Long, y: Long)
}

open class StormTrooper : Trooper {
    override fun move(x: Long, y: Long) {
        // Move at normal speed
    }

    override fun attackRebel(x: Long, y: Long) {
        // Missed most of the time
    }
}

open class ShockTrooper : Trooper {
    override fun move(x: Long, y: Long) {
        // Moves slower than Storm Trooper
    }

    override fun attackRebel(x: Long, y: Long) {
        // Sometimes hits
    }
}

class RiotControlTrooper : StormTrooper() {
    override fun attackRebel(x: Long, y: Long) {
        // Has an electric baton, stay away!
    }
}

class FlameTrooper : ShockTrooper() {
    override fun attackRebel(x: Long, y: Long) {
        // Uses flametrower, dangerous!
    }
}

class ScoutTrooper: ShockTrooper() {
    override fun move(x: Long, y: Long) {
        // Runs faster
    }
}

// That's a lot of classes

// One day, our dear designer comes and asks that all stormtroopers should be able to shout and
// each will have a different phrase . Without thinking twice, we add a new function to our inteface:


//interface Trooper {
//    fun move(x: Long, y: Long)
//    fun attackRebel(x: Long, y: Long)
//    fun shout(): String
//}

// By doing that, all the classes that implements this interface stop compiling. And we have a lot of
// them. That's a lot of changes that we'll have to make. So, we'll just have to suck it up and
// get to work.

// Or will we? What if there were 50 classes ?







// BRIDGING CHANGES

// The idea behind the bridge design pattern is to flatten the class hierarchy and have fewer
// specialized classes in our system. It also helps us to avoid the fragile base class problem
// when modifying the superclass introduces subtle bugs to classes that inherit from it.

// First, let's try to understand why we have this complex hierarchy and many classes. It's because
// we have two orthogonal, unrelated properties: weapon type and movement speed.

// Let's say that instead, we wanted to pass those properties to the constructor of a class
// that implements the same interface we have been using all along:
class StormTrooper2(
    private val weapon: Weapon,
    private val legs: Legs,
    private val shout: Shout,
) : Trooper {
    override fun move(x: Long, y: Long) {
        legs.move(x, y)
    }

    override fun attackRebel(x: Long, y: Long) {
        weapon.attack(x, y)
    }
    fun shout() = println(shout.shout())
}

// The properties that StormTrooper2 receives should be interfaces, so we can choose their
// implementations later:
typealias PointsOfDamage = Long // typealias is alternative name for existing type
typealias Meters = Int
interface Weapon {
    fun attack(x: Long, y: Long): PointsOfDamage
}
interface Legs {
    fun move(x: Long, y: Long): Meters
}
interface Shout {
    fun shout(): String
}

// Notice that these two methods return Meters and PointsOfDamage instead of simply returning Long and
// Int . This featue is called type aliasing. To understand how this works, take a look at typealias.txt

// Let's go back to our stormtrooper class. It's time to provide some implementations for the Weapon
//and Legs interfaces

// First, let's define the regular damage and speed of StormTrooper, using imperial units:

const val RIFLE_DAMAGE = 3L
const val REGULAR_SPEED: Meters = 1

//mThese values are very effective since they are known during compilation. Unlike static final
// variables in Java, they cannot be placed inside a class. You should place them either at
// the top level of your package or nest them inside an object.

// Now, we can provide some implementations for our interfaces:
class Rifle: Weapon {
    override fun attack(x: Long,y: Long): PointsOfDamage = RIFLE_DAMAGE
}
class Flamethrower: Weapon {
    override fun attack(x: Long, y: Long): PointsOfDamage = RIFLE_DAMAGE * 2
}
class Baton: Weapon {
    override fun attack(x: Long, y: Long): PointsOfDamage = RIFLE_DAMAGE * 3
}
// Next, let's look at how we can move the following:
class RegularLegs: Legs {
    override fun move(x: Long, y: Long): Meters = REGULAR_SPEED
}
class AthleticLegs: Legs {
    override fun move(x: Long, y: Long): Meters = REGULAR_SPEED * 2
}
class HeilHitler: Shout {
    override fun shout() = "Heil Hitler !"
}
class LongLiveIndia: Shout {
    override fun shout() = "Long Live India!"
}
// ---------------------- THIS IS WHERE THE MAGIC HAPPENS ------------------------
// Finally, we need to make sure that we can implement the same functionality without the complex
// class hierarchy we had before:
fun main() {
    val stormTrooper = StormTrooper2(Rifle(),RegularLegs(), HeilHitler())
    val flameTrooper = StormTrooper2(Flamethrower(), RegularLegs(), LongLiveIndia())
    val scoutTrooper = StormTrooper2(Rifle(), AthleticLegs(), LongLiveIndia())

    stormTrooper.shout()
}

// Now we have a flat class hierarchy, which is much simpler to extend and also to understand.
// If we need more functionality, such as the shouting ability we mentioned earlier, we would
// add a new interface and a new constructor argument for our class.