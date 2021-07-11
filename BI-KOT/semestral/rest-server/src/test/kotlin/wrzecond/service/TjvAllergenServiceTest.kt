package wrzecond.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import wrzecond.TjvAllergenCreateDTO
import wrzecond.TjvAllergenReadDTO
import wrzecond.TjvAllergenUpdateDTO
import wrzecond.entity.TjvAllergen
import wrzecond.repository.TjvAllergenRepository

class TjvAllergenServiceTest : StringSpec ({
    val mockRepository = mockk<TjvAllergenRepository>()
    val service = TjvAllergenService(mockRepository)

    "toDTO" {
        val allergen = TjvAllergen("Wheat", 42)
        service.run { allergen.toDTO() } shouldBe TjvAllergenReadDTO(allergen.id, allergen.name)
    }

    "fromDTO" {
        val dto = TjvAllergenCreateDTO("Wheat")
        service.run { dto.toEntity() } shouldBe TjvAllergen(dto.name, 0)
    }

    "merge" {
        val allergen1 = TjvAllergen("Wheat", 1)
        val dto1 = TjvAllergenUpdateDTO(null)
        service.run { allergen1.merge(dto1) } shouldBe allergen1

        val dto2 = TjvAllergenUpdateDTO("Milk")
        service.run { allergen1.merge(dto2) } shouldBe TjvAllergen("Milk", 1)
    }
})