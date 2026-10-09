package com.example

import com.example.data.CourseRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CourseRepositoryTest {

    private lateinit var repository: CourseRepository

    @Before
    fun setUp() {
        repository = CourseRepository()
    }

    @Test
    fun testLessonSearchQueryFiltering() {
        // Initial state should have blank search query
        assertEquals("", repository.lessonSearchQuery.value)

        // Set search query for "hooks"
        repository.updateLessonSearchQuery("hooks")
        assertEquals("hooks", repository.lessonSearchQuery.value)

        // Filter modules by "hooks"
        val allModules = repository.modules.value
        val filtered = allModules.filter { mod ->
            mod.title.contains("hooks", ignoreCase = true) ||
            mod.lessons.any { it.title.contains("hooks", ignoreCase = true) || it.summary.contains("hooks", ignoreCase = true) }
        }
        assertTrue(filtered.isNotEmpty())
    }

    @Test
    fun testLessonCompletionProgress() {
        val (completedBefore, total) = repository.getProgressStats()
        assertTrue(total > 0)

        // Toggle first lesson of first module
        val firstMod = repository.modules.value.first()
        val firstLes = firstMod.lessons.first()
        val initialStatus = firstLes.isCompleted

        repository.toggleLessonCompletion(firstMod.id, firstLes.id)
        val updatedMod = repository.modules.value.first { it.id == firstMod.id }
        val updatedLes = updatedMod.lessons.first { it.id == firstLes.id }

        assertEquals(!initialStatus, updatedLes.isCompleted)
    }

    @Test
    fun testAdminOperations() {
        val initialCount = repository.modules.value.size
        repository.addNewModule("AI Tools in 2026", "Mastering automated content generation")
        assertEquals(initialCount + 1, repository.modules.value.size)

        val lastModule = repository.modules.value.last()
        assertTrue(lastModule.title.contains("AI Tools in 2026"))

        // Add lesson to new module
        repository.addLessonToModule(lastModule.id, "Prompting for Viral Scripts", 20, "Summary")
        val reloadedMod = repository.modules.value.first { it.id == lastModule.id }
        assertEquals(1, reloadedMod.lessons.size)

        // Delete module
        repository.deleteModule(lastModule.id)
        assertEquals(initialCount, repository.modules.value.size)
    }
}
