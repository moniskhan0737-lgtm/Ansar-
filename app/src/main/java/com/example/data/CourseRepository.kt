package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Central repository managing Course, Modules, Lessons, Progress, Search, and Admin operations.
 * Designed to be seamlessly plugged into Firebase Firestore when needed.
 */
class CourseRepository {

    private val _courseInfo = MutableStateFlow(SampleCourseData.courseInfo)
    val courseInfo: StateFlow<CourseInfo> = _courseInfo.asStateFlow()

    private val _modules = MutableStateFlow<List<CourseModule>>(SampleCourseData.initialModules)
    val modules: StateFlow<List<CourseModule>> = _modules.asStateFlow()

    private val _testimonials = MutableStateFlow<List<Testimonial>>(SampleCourseData.sampleTestimonials)
    val testimonials: StateFlow<List<Testimonial>> = _testimonials.asStateFlow()

    private val _enrolledStudents = MutableStateFlow<List<EnrollmentRecord>>(SampleCourseData.sampleEnrolledStudents)
    val enrolledStudents: StateFlow<List<EnrollmentRecord>> = _enrolledStudents.asStateFlow()

    // Student Dashboard Lesson & Module Search query
    private val _lessonSearchQuery = MutableStateFlow("")
    val lessonSearchQuery: StateFlow<String> = _lessonSearchQuery.asStateFlow()

    // Filter by module category or completion state
    private val _filterOnlyIncomplete = MutableStateFlow(false)
    val filterOnlyIncomplete: StateFlow<Boolean> = _filterOnlyIncomplete.asStateFlow()

    fun updateLessonSearchQuery(query: String) {
        _lessonSearchQuery.value = query
    }

    fun toggleFilterOnlyIncomplete() {
        _filterOnlyIncomplete.update { !it }
    }

    /**
     * Toggles completion status of a lesson. Updates dynamic progress percentage.
     */
    fun toggleLessonCompletion(moduleId: String, lessonId: String) {
        _modules.update { currentModules ->
            currentModules.map { module ->
                if (module.id == moduleId) {
                    val updatedLessons = module.lessons.map { lesson ->
                        if (lesson.id == lessonId) {
                            lesson.copy(isCompleted = !lesson.isCompleted)
                        } else {
                            lesson
                        }
                    }
                    module.copy(lessons = updatedLessons)
                } else {
                    module
                }
            }
        }
    }

    /**
     * Compute total lessons and completed lessons across all modules
     */
    fun getProgressStats(): Pair<Int, Int> {
        val allLessons = _modules.value.flatMap { it.lessons }
        val completed = allLessons.count { it.isCompleted }
        return Pair(completed, allLessons.size)
    }

    fun getProgressPercentage(): Float {
        val (completed, total) = getProgressStats()
        if (total == 0) return 0f
        return (completed.toFloat() / total.toFloat()) * 100f
    }

    /**
     * Finds the next uncompleted lesson to power the "Continue Learning" button
     */
    fun getNextUncompletedLesson(): Pair<CourseModule, CourseLesson>? {
        for (module in _modules.value) {
            val nextLesson = module.lessons.firstOrNull { !it.isCompleted }
            if (nextLesson != null) {
                return Pair(module, nextLesson)
            }
        }
        // If all completed, return first lesson of first module
        val firstMod = _modules.value.firstOrNull() ?: return null
        val firstLes = firstMod.lessons.firstOrNull() ?: return null
        return Pair(firstMod, firstLes)
    }

    // --- Admin Operations ---

    fun addNewModule(title: String, description: String) {
        val current = _modules.value
        val newModNum = current.size + 1
        val newMod = CourseModule(
            id = "mod_${System.currentTimeMillis()}",
            number = newModNum,
            title = "$newModNum. $title",
            description = description,
            lessons = emptyList()
        )
        _modules.update { it + newMod }
    }

    fun deleteModule(moduleId: String) {
        _modules.update { current ->
            current.filter { it.id != moduleId }.mapIndexed { index, mod ->
                mod.copy(number = index + 1)
            }
        }
    }

    fun addLessonToModule(moduleId: String, title: String, durationMinutes: Int, summary: String) {
        _modules.update { current ->
            current.map { mod ->
                if (mod.id == moduleId) {
                    val newLesson = CourseLesson(
                        id = "les_${System.currentTimeMillis()}",
                        moduleId = moduleId,
                        title = title,
                        durationMinutes = durationMinutes,
                        summary = summary,
                        isCompleted = false,
                        keyTakeaways = listOf("Key lesson concept and practical implementation tip.")
                    )
                    mod.copy(lessons = mod.lessons + newLesson)
                } else {
                    mod
                }
            }
        }
    }

    fun deleteLesson(moduleId: String, lessonId: String) {
        _modules.update { current ->
            current.map { mod ->
                if (mod.id == moduleId) {
                    mod.copy(lessons = mod.lessons.filter { it.id != lessonId })
                } else {
                    mod
                }
            }
        }
    }

    fun addTestimonial(name: String, role: String, comment: String, resultBadge: String) {
        val newTestimonial = Testimonial(
            id = "test_${System.currentTimeMillis()}",
            studentName = name,
            handle = "@${name.lowercase().replace(" ", "")}",
            avatarEmoji = "⭐",
            role = role,
            rating = 5,
            commentHinglish = comment,
            resultBadge = resultBadge,
            isSampleEditable = true
        )
        _testimonials.update { listOf(newTestimonial) + it }
    }

    fun deleteTestimonial(id: String) {
        _testimonials.update { it.filter { test -> test.id != id } }
    }

    fun recordNewEnrollment(record: EnrollmentRecord) {
        _enrolledStudents.update { listOf(record) + it }
    }

    // Admin Analytics KPIs
    fun getAdminStats(): Triple<Int, Int, Int> {
        val totalStudents = _enrolledStudents.value.size
        val totalSales = totalStudents
        val grossRevenue = _enrolledStudents.value.sumOf { it.amount }
        return Triple(totalStudents, totalSales, grossRevenue)
    }

    companion object {
        // Singleton repository instance for app-wide access
        val instance: CourseRepository by lazy { CourseRepository() }
    }
}
