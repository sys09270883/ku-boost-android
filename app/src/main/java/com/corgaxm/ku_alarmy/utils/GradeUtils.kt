package com.corgaxm.ku_alarmy.utils

import android.util.Log
import com.corgaxm.ku_alarmy.persistence.GradeEntity
import kotlin.math.floor

object GradeUtils {

    // 각 학기별 평균
    fun average(allGrades: List<GradeEntity>): List<String> {
        val keys = sortedSetOf<String>()
        val sum = sortedMapOf<String, Float>()
        val point = sortedMapOf<String, Int>()
        val result = sortedMapOf<String, String>()

        for (grade in allGrades) {
            val key = "${grade.year}${grade.semester}"
            val pnt = grade.subjectPoint
            val grd = grade.grade
            keys.add(key)
            sum.putIfAbsent(key, 0.0f)
            point.putIfAbsent(key, 0)
            sum[key] = sum[key]!! + pnt * grd
            point[key] = point[key]!! + pnt
        }

        for (key in keys) {
            try {
                result[key] = "%.2f".format(floor((sum[key]!! / point[key]!!) * 100) / 100.0)
            } catch (exception: Exception) {
                Log.e("yoonseop", "${exception.message}")
            }

        }

        return result.values.toList()
    }

    // 취득 학점 분포(알파벳)
    fun characterGrades(allGrades: List<GradeEntity>): Map<String, Float> {
        val result = sortedMapOf<String, Float>()

        for (grade in allGrades) {
            val key = grade.characterGrade
            result.putIfAbsent(key, 0.0f)
            result[key] = result[key]!! + 1
        }

        return result.toList().sortedByDescending { (_, value) -> value }.toMap()
    }
}