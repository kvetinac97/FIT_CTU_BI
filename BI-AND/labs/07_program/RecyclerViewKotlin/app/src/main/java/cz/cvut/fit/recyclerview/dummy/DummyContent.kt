package cz.cvut.fit.recyclerview.dummy

import com.thedeanda.lorem.LoremIpsum

import java.util.ArrayList

object DummyContent {

    private val lorem = LoremIpsum.getInstance()
    private var maxId: Long = 1
    val ACTOR_ENTITIES: MutableList<ActorEntity> = ArrayList(listOf(
        addItem("Jiří", "Šebánek"),
        addItem("Miloň", "Čepelka"),
        addItem("Ladislav", "Smoljak"),
        addItem("Zdeněk", "Svěrák"),
        addItem("Karel", "Velebný"),
        addItem("Jan", "Trtílek"),
        addItem("Oldřich", "Unger"),
        addItem("Josef", "Podaný"),
        addItem("Jiří", "Stivín"),
        addItem("Ivan", "Štědrý"),
        addItem("Vít", "Mach"),
        addItem("Pavel", "Vondruška"),
        addItem("Jaroslav", "Vozáb"),
        addItem("Jan", "Klusák"),
        addItem("Petr", "Brukner"),
        addItem("Jaroslav", "Weigel"),
        addItem("František", "Petiška"),
        addItem("Václav", "Kotek"),
        addItem("Jan", "Kašpar"),
        addItem("Jiří", "Menzel"),
        addItem("Jaroslav", "Uhlíř"),
        addItem("Josef", "Koudelka"),
        addItem("Jan", "Hraběta"),
        addItem("Bořivoj", "Penc"),
        addItem("Genadij", "Rumlena"),
        addItem("Marek", "Šimon"),
        addItem("Petr", "Reidinger"),
        addItem("Michal", "Weigel"),
        addItem("Robert", "Bárta"),
        addItem("Milan", "Svoboda"),
        addItem("Andrej", "Krob")
    ))

    private fun addItem (name: String, surname: String)
        = ActorEntity(maxId++, name, surname, lorem.getParagraphs(1, 3))

    /**
     * A dummy item representing a piece of content.
     */
    data class ActorEntity(val id: Long, val name: String, val surname: String, val description: String)
}
