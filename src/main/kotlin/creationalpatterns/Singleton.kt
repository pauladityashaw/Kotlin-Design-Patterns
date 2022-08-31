package creationalpatterns

object Singleton : List<String> { // object keyword is used to make a class a singleton
    //singleton is called object in kotlin. singleton/object is not = class in kotlin

    // a kotlin singleton(also called kotlin object) can't have a constructor
    // If you need to implement initialization for your Singleton, such as loading from a configuration
    // file for the 1st time, you can use the init block

    init {
        println("I was accessed for the 1st time")
    }
    // init block is run only when the singleton is instantiated/accessed for the 1st time

    override val size = 0

    override fun contains(element: String): Boolean = false

    override fun containsAll(elements: Collection<String>): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(index: Int): String {
        TODO("Not yet implemented")
    }

    override fun indexOf(element: String): Int {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<String> {
        TODO("Not yet implemented")
    }

    override fun lastIndexOf(element: String): Int {
        TODO("Not yet implemented")
    }

    override fun listIterator(): ListIterator<String> {
        TODO("Not yet implemented")
    }

    override fun listIterator(index: Int): ListIterator<String> {
        TODO("Not yet implemented")
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<String> {
        TODO("Not yet implemented")
    }
}