package com.konkuk.boost.utils

import android.util.Log
import com.konkuk.boost.persistence.GradeEntity
import kotlin.math.floor

object GradeUtils {

    val classifications =
        hashSetOf("기타", "일선", "일교", "기교", "핵교", "지교", "전필", "전선", "다선", "다지", "다필", "지필")

    // 각 학기별 평균
    fun average(allGrades: List<GradeEntity>): List<String> {
        val keys = sortedSetOf<String>()
        val sum = sortedMapOf<String, Float>()
        val point = sortedMapOf<String, Int>()
        val result = sortedMapOf<String, String>()

        for (grade in allGrades) {
            if (grade.characterGrade == "P" || grade.characterGrade == "N")
                continue

            val key = "${grade.year}${grade.semester}"
            val pnt = grade.subjectPoint
            val grd = grade.grade
            keys.add(key)

            if (!sum.containsKey(key))
                sum[key] = 0.0f
            sum[key] = sum[key]!! + pnt * grd

            if (!point.containsKey(key))
                point[key] = 0
            point[key] = point[key]!! + pnt
        }

        for (key in keys) {
            try {
                result[key] = "%.2f".format(floor((sum[key]!! / point[key]!!) * 100) / 100.0)
                if (result[key] == "NaN") result[key] = "0.00"
            } catch (exception: Exception) {
                Log.e("ku-boost", "${exception.message}")
            }
        }

        return result.values.toList()
    }

    // 취득 학점 분포(알파벳)
    fun characterGrades(allGrades: List<GradeEntity>): Map<String, Float> {
        val result = sortedMapOf<String, Float>()

        for (grade in allGrades) {
            val key = grade.characterGrade
            if (!result.containsKey(key))
                result[key] = 0.0f

            result[key] = result[key]!! + 1
        }

        return result.toList().sortedByDescending { (_, value) -> value }.toMap()
    }

    fun semesters(allGrades: List<GradeEntity>): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        val isUsed = hashMapOf<Pair<Int, Int>, Boolean>()

        for (grade in allGrades) {
            val key = Pair(grade.year, grade.semester)
            if (!isUsed.containsKey(key))
                isUsed[key] = false

            if (!isUsed[key]!!) {
                result.add(Pair(grade.year, grade.semester))
                isUsed[key] = true
            }
        }

        return result.toList()
    }

    // 전체 학기 평균 및 전공 평균
    // 삭제된 과목이거나 성적이 인정되지 않은 학기는 제외
    fun totalAverages(allGrades: List<GradeEntity>): Pair<String, String> {
        var sum = 0.0f
        var majorSum = 0.0f
        var point = 0
        var majorPoint = 0

        for (grade in allGrades) {
            if (grade.characterGrade == "P" || grade.characterGrade == "N")
                continue

            val pnt = grade.subjectPoint
            val grd = grade.grade

            sum += pnt * grd
            point += pnt

            if (grade.classification.startsWith("전")) {
                majorSum += pnt * grd
                majorPoint += pnt
            }
        }

        var avr = "%.2f".format(floor((sum / point) * 100) / 100.0)
        var majorAvr = "%.2f".format(floor((majorSum / majorPoint) * 100) / 100.0)
        if (avr == "NaN") avr = "0.00"
        if (majorAvr == "NaN") majorAvr = "0.00"

        return Pair(avr, majorAvr)
    }

    fun translate(semester: Int): String {
        val mapper = hashMapOf(1 to "1", 2 to "하계계절", 3 to "2", 4 to "동계계절")
        return mapper[semester]!!
    }

    fun isClassification(classification: String): Boolean = classifications.contains(classification)
}