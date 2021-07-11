package wrzecond.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Sort
import org.springframework.web.server.ResponseStatusException
import wrzecond.TjvAllergenCreateDTO
import wrzecond.TjvAllergenReadDTO
import wrzecond.TjvAllergenUpdateDTO
import wrzecond.entity.TjvAllergen
import wrzecond.repository.TjvAllergenRepository
import javax.persistence.EntityNotFoundException

/**
 * This class tests TjvService.
 * As TjvService is generic template class,
 * I use TjvAllergenService for testing it.
 */
class TjvServiceTest : StringSpec ({
    // Init
    val mockRepository = mockk<TjvAllergenRepository>(relaxed = true)
    val service = TjvAllergenService(mockRepository)

    "getEntityByID" {
        val allergen = TjvAllergen("Wheat", 42)
        every { mockRepository.getOne(allergen.id) } returns allergen
        every { mockRepository.getOne(1000) } throws EntityNotFoundException()

        service.getEntityByID(allergen.id) shouldBe allergen
        verify { mockRepository.getOne(allergen.id) }
        confirmVerified(mockRepository)

        shouldThrow<ResponseStatusException> {
            service.getEntityByID(1000)
        }
        verify { mockRepository.getOne(1000) }
        confirmVerified(mockRepository)
    }
    "getByID" {
        val allergen = TjvAllergen("Milk", 1)
        val dto = TjvAllergenReadDTO(allergen.id, allergen.name)

        every { mockRepository.getOne(allergen.id) } returns allergen
        every { mockRepository.getOne(2000) } throws EntityNotFoundException()

        service.getByID(allergen.id) shouldBe dto
        verify { mockRepository.getOne(allergen.id) }
        confirmVerified(mockRepository)

        shouldThrow<ResponseStatusException> {
            service.getEntityByID(2000)
        }
        verify { mockRepository.getOne(2000) }
        confirmVerified(mockRepository)
    }
    "findByIDs" {
        val allergen1 = TjvAllergen("Cocoa", 10)
        val allergen2 = TjvAllergen("Beans", 12)

        every { mockRepository.getOne(allergen1.id) } returns allergen1
        every { mockRepository.getOne(allergen2.id) } returns allergen2

        service.findByIDs(listOf(allergen1.id, allergen2.id)) shouldContainExactlyInAnyOrder listOf(allergen1, allergen2)
        verify { mockRepository.getOne(allergen1.id) }
        verify { mockRepository.getOne(allergen2.id) }
        confirmVerified(mockRepository)
    }
    "findAll" {
        val allergen1 = TjvAllergen("Gluten", 24)
        val allergen2 = TjvAllergen("Cats", 25)
        val dto1 = TjvAllergenReadDTO(allergen1.id, allergen1.name)
        val dto2 = TjvAllergenReadDTO(allergen2.id, allergen2.name)

        every { mockRepository.findAll(any<Sort>()) } returns listOf(allergen1, allergen2)
        service.findAll() shouldContainExactlyInAnyOrder listOf(dto1, dto2)
        verify { mockRepository.findAll(any<Sort>()) }
        confirmVerified(mockRepository)
    }
    "create" {
        val allergen1 = TjvAllergen("School", 0)
        val allergen2 = TjvAllergen("C++", 0)
        val create1 = TjvAllergenCreateDTO(allergen1.name)
        val create2 = TjvAllergenCreateDTO(allergen2.name)
        val dto = TjvAllergenReadDTO(allergen1.id, allergen1.name)

        every { mockRepository.save(allergen1) } returns allergen1
        every { mockRepository.save(allergen2) } throws DataIntegrityViolationException("")

        service.create(create1) shouldContainExactlyInAnyOrder listOf(dto)
        verify { mockRepository.save(allergen1) }
        verify { mockRepository.flush() }
        confirmVerified(mockRepository)

        shouldThrow<ResponseStatusException> {
            service.create(create2)
        }
        verify { mockRepository.save(allergen2) }
        confirmVerified(mockRepository)
    }
    "update" {
        val allergen1 = TjvAllergen("Babish", 900)
        val allergen2 = TjvAllergen("Faltynek", 911)
        val allergen3 = TjvAllergen("Repka", 900)

        val update1 = TjvAllergenUpdateDTO("Repka")
        val update2 = TjvAllergenUpdateDTO(null)
        val update3 = TjvAllergenUpdateDTO("Stone")

        every { mockRepository.getOne(allergen1.id) } returns allergen1
        every { mockRepository.getOne(allergen2.id) } returns allergen2
        every { mockRepository.getOne(2000) } throws EntityNotFoundException()
        every { mockRepository.saveAndFlush(allergen3) } returns allergen3
        every { mockRepository.saveAndFlush(allergen2) } throws DataIntegrityViolationException("")

        service.update(allergen1.id, update1)
        verify { mockRepository.getOne(allergen1.id) }
        verify { mockRepository.saveAndFlush(allergen3) }
        confirmVerified(mockRepository)

        shouldThrow<ResponseStatusException> {
            service.update(allergen2.id, update2)
        }
        verify { mockRepository.getOne(allergen2.id) }
        verify { mockRepository.saveAndFlush(allergen2) }
        confirmVerified(mockRepository)

        shouldThrow<ResponseStatusException> {
            service.update(2000, update3)
        }
        verify { mockRepository.getOne(2000) }
        confirmVerified(mockRepository)
    }
    "delete" {
        every { mockRepository.deleteById(144) } returns Unit
        every { mockRepository.deleteById(142) } throws EntityNotFoundException()

        service.delete(144)
        verify { mockRepository.deleteById(144) }
        confirmVerified(mockRepository)

        shouldThrow<ResponseStatusException> {
            service.delete(142)
        }
        verify { mockRepository.deleteById(142) }
        confirmVerified(mockRepository)
    }

})