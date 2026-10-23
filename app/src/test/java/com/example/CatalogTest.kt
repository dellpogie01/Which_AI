package com.example

import com.example.data.AiRepository
import org.junit.Assert.*
import org.junit.Test

class CatalogTest {
    @Test fun catalogEntriesHaveUniqueIdsValidCategoriesAndOfficialLinks() {
        val models = AiRepository.aiModels
        assertTrue(models.size >= 79)
        assertEquals(models.size, models.map { it.id }.toSet().size)
        models.forEach {
            assertTrue(it.name.isNotBlank())
            assertTrue(it.category in AiRepository.categories.drop(1))
            assertTrue(it.webLink.startsWith("https://"))
            assertTrue(it.whatItDoes.isNotBlank())
            assertTrue(it.bestFor.isNotEmpty())
        }
    }
}
