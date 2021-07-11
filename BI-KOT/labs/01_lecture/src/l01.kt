// Neplatí jeden soubor = jedna třída

// Data class: automatický konstruktor, equals, toString...
// Nullable type <3, kontrola PŘÍMO PŘI COMPILE TIME, default hodnoty, typově striktní
data class Person(val name: String, var age: Int? = null)

// Funkce nemusí být vložena do třídy
fun main (args: Array<String>) {
    // Není třeba žádný new ArrayList, val ("automatický typ"), named arguments
    val persons = listOf(Person("Alice"), Person("Bob", age = 29))

    // lambda výrazy, operátor ?: ( it.age != null ? it.age : 0 )
    val oldest = persons.maxByOrNull { it.age ?: 0 }
    // vložení parametrů do stringu
    val str: Any = "The oldest is: $oldest"

    // Chytrá typová inference
    if ( str is String )
        println(str)
}
