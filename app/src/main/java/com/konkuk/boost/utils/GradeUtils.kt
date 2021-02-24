package com.konkuk.boost.utils

import android.util.Log
import com.konkuk.boost.persistence.grade.GradeEntity
import kotlin.math.floor

object GradeUtils {

    fun core(area: String): String {
        var str = area.replace("(", "")
        str = str.replace(")", "")
        str = str.replace("E", "")

        return when (str) {
            "문" -> "문화예술영역"
            "사" -> "사회과학영역"
            "인" -> "인문과학영역"
            "자" -> "자연과학기술융합영역"
            "외" -> "제2외국어영역"
            "사고" -> "사고력증진"
            "인재" -> "글로벌인재양성"
            "학문" -> "학문소양및인성함양"
            else -> ""
        }
    }

    fun basic(area: String) = when (area[1]) {
        'S' -> "SW"
        '글' -> "글쓰기"
        '취' -> "취창업"
        '외' -> "외국어"
        '인' -> "인성"
        '교' -> "교양영어"
        else -> ""
    }

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
        return when (semester) {
            1 -> "1"
            2 -> "하계계절"
            3 -> "2"
            4 -> "동계계절"
            else -> throw Exception("Semester invalid error: $semester")
        }
    }

    fun convertToSemesterCode(semester: Int): String {
        val sem = when (semester) {
            1 -> 1
            2 -> 4
            3 -> 2
            4 -> 5
            else -> throw Exception("Semester invalid error: $semester")
        }
        return "B0101$sem"
    }

    fun convertToKorean(englishKey: String) = when (englishKey) {
        "zipCode" -> "우편번호"
        "cellPhoneNo" -> "핸드폰번호"
        "enterDate" -> "입학일자"
        "chineseName" -> "한자이름"
        "schoolYear" -> "학년"
        "email" -> "이메일"
        "highSchoolName" -> "출신고교"
        "universityName" -> "출신대학"
        "gender" -> "성별"
        "highSchoolGraduationDate" -> "고교졸업일자"
        "earlyGraduationAvailability" -> "조기졸업"
        "country" -> "국적"
        "tellNo" -> "집전화번호"
        "koreanName" -> "이름"
        "englishName" -> "영어이름"
        "birthday" -> "생일"
        "studentDiv" -> "구분"
        "enterCode" -> "입학구분"
        "address" -> "주소"
        "impairment" -> "장애여부"
        else -> ""
    }
}