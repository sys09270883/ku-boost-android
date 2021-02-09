package com.konkuk.boost

import org.junit.Test

class GradeUnitTest {

    /* 금학기: 2021년 3학기 */
    private fun trueIf_2021_3_Before(year: Int, semester: Int): Boolean {
        if (year >= 2021 && semester > 3)
            return false
        return true
    }

    @Test
    fun `grade_getLatestSemester`() {
        val years = listOf(2015, 2019, 2020, 2021)
        val semesters = listOf(4, 3, 2, 1)

        var isLatestSemesterQueried = false
        var latestYear = 0
        var latestSemester = 0

        for (year in years) {
            for (semester in semesters) {
                if (year == years.last() && !isLatestSemesterQueried && trueIf_2021_3_Before(year, semester)) {
                    isLatestSemesterQueried = true
                    latestYear = year
                    latestSemester = semester
                }
            }
        }

        assert(latestYear == 2021 && latestSemester == 3)
    }
}