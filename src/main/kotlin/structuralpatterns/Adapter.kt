package structuralpatterns

// ADAPTER - The main goal of the Adapter design pattern is to convert one interface to another
// interface. In the physical world, the best example of this idea would be an electrical plug
// adapter.

// The Adapter design pattern helps you to work with Legacy code.

// Imagine yourself in a hotel room late in the evening, with 7% battery left on your phone.
// Your phone charger was left in the office at the other end of the city. You only have an EU plug
// charger with a Mini USB cable. But your phone uses USB-C, as you had to upgrade.
// You're in New York, so all of your outlets are (of course) USB-A. So, what do you do?

// Oh, it's easy. You look for a Mini USB to USB-C adapter in the middle of the night and
// hope that you have remembered to bring your EU to US plug adapter as well.
// Only 5% battery left – time is running out!
// So, now that we understand what adapters are for in the physical world,
// let's see how we can apply the same principle in code.

// Let's start with interfaces.

// USPlug assumes the power is Int. It has 1 as its value if it has power and any other value
// if it doesn't
interface USPlug {
    val hasPower: Int
}

// EUPlug treats power as String, which is either TRUE or FALSE
interface EUPlug {
    val hasPower: String // "TRUE" or "FALSE"
}

// For USB-mini, power is an enum:
interface UsbMini {
    val hasPower: Power
}

enum class Power {
    TRUE, FALSE
}

// Finally, for USB-TypeC, power is a boolean value:
interface UsbTypeC {
    val hasPower: Boolean
}

//Our goal is to bring the power value from a US power outlet to our cellphone, which will be represented
//by this function:
fun cellPhone(chargeCable: UsbTypeC) {
    if (chargeCable.hasPower) {
        println("I've Got The Power!")
    } else {
        println("No power")
    }
}

//Let's start by declaring what a US power outlet will look like in our code. It will be a function
//that returns a USPlug:
// Power outlet exposes USPlug interface
fun usPowerOutlet(): USPlug {
    return object : USPlug {
        override val hasPower = 1
    }
}

//In the previous chapter, we discussed two different uses of the object keyword. In the global scope,
//it creates a Singleton object. When used together with the companion keyword inside of a class, it
//creates a place for defining static functions. The same keyword can also be used to generate anonymous
//classes. Anonymous classes are classes that are created on the fly, usually to implement an
//interface in an ad-hoc manner.

// Our charger will be a function that takes EUPlug as an input and outputs UsbMini:
// Charges accepts EUPlug interface and exposes UsbMini interface
fun charger(plug: EUPlug): UsbMini {
    return object : UsbMini {
        override val hasPower = Power.valueOf(plug.hasPower)
    }
}

// Next, let's try to combine our cellPhone, charger and usPowerOutlet functions:
fun main() {
    // our cellPhone takes in USB-Type C - but USB Mini was provided
    // our charger required and EU Plug , but US Plug was provided
    // uncomment lines 85 to 91
//    cellPhone(
//        // Type mismatch. Required: UsbTypeC, Found: UsbMini
//        charger(
//            // Type mismatch. Required: EUPlug, Found: USPlug
//            usPowerOutlet()
//        )
//    )
    // using the adapter design pattern we intend to solve this problem...solving below
    // from line 96 to 114

    // Finally, we can charge our cell phones:
    cellPhone(charger(usPowerOutlet().toEUPlug()).toUsbTypeC())
}

// We need 2 types of adapter: 1 for our Power Plug and another for our USB port
// In Java, you would usually create a pair of classes for this purpose. In Kotlin, we can
// replace these classes with extension functions. We briefly covered extension function in
// MailBuilder pattern

// We could adapt the US Plug to work with the EU Plug by defining the following extension function:
fun USPlug.toEUPlug(): EUPlug {
    val hasPower = if(this.hasPower == 1) "TRUE" else "FALSE"
    return object: EUPlug {
        override val hasPower = hasPower
    }
}
// We can create a USB adapter between the Mini USB and Type-C USB in a similar way:
fun UsbMini.toUsbTypeC(): UsbTypeC {
    val hasPower = this.hasPower == Power.TRUE
    return object: UsbTypeC {
        override val hasPower = hasPower
    }
}