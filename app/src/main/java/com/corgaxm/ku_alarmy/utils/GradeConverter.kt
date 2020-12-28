package com.corgaxm.ku_alarmy.utils

object GradeConverter {
    val mapper = hashMapOf(
        "basicElective" to "기교",
        "generalElective" to "지교",
        "coreElective" to "핵교",
        "normalElective" to "일교",
        "generalRequirement" to "지필",
        "majorRequirement" to "전필",
        "majorElective" to "전선",
        "dualElective" to "다지",
        "dualRequirement" to "다필",
        "dualMajorElective" to "다선",
        "etc" to "기타",
        "total" to "총점"
    )

    fun convert(englishGradeClassification: String) = mapper[englishGradeClassification]!!
}