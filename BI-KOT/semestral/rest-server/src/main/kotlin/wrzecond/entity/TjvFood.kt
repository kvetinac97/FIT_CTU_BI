package wrzecond.entity

import javax.persistence.*

@Entity
data class TjvFood (
    @Column(nullable = false) val name: String,
    @Column(nullable = false) val price: Int,
    @Column(nullable = false) val cooked: Boolean,

    @ManyToMany
    @JoinTable (
        name = "TJV_FOOD_ALLERGEN",
        joinColumns = [JoinColumn(name = "food_id")],
        inverseJoinColumns = [JoinColumn(name = "allergen_id")]
    )
    val allergens: List<TjvAllergen>,
    override val id: Int = 0
) : TjvEntity()
