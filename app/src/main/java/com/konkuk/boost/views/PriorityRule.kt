package com.konkuk.boost.views

object PriorityRule {
    fun personalInfoRule(key: String): Int = when (key) {
        "koreanName" -> 100
        "englishName", "chineseName" -> 99
        "schoolRegisteredState" -> 98
        "dept", "major" -> 97
        "schoolYear" -> 96
        "birthday" -> 95
        "cellPhoneNo", "email" -> 94
        "zipCode" -> 93
        "address" -> 92
        else -> 0
    }
}