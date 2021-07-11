fun main () {
    // Immutable collection
    val x = listOf("Test", "Test2")
    println(x)
    // x.add("Test3") failes to compile

    // Mutable collection
    val y = mutableListOf("Test", "Test2")
    println(y)
    y.add("Test3")
    println(y)

    // Sequences are lazy
    val list1 = sequenceOf(1, 2, 3)
        .filter { println("Filter $it"); it % 2 == 1 }
        .map { println("Map $it"); it * 2 }
        .toList()
    println(list1)

    val list2 = listOf(1, 2, 3)
        .filter { println("Filter $it"); it % 2 == 1 }
        .map { println("Map $it"); it * 2 }
    println(list2)

    // Infinite sequence
    var index = 0
    val seqIterator = generateSequence { index++ }.iterator()
    while (!seqIterator.hasNext()) {
        println(seqIterator.next())
        Thread.sleep(500)
    }
}