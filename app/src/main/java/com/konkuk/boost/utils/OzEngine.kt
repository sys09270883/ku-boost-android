package com.konkuk.boost.utils


object OzEngine {

    fun generate(stdNo: String) {
        val hexStdNo = stdNOtoHex(stdNo)
    }

    private fun stdNOtoHex(stdNo: String): String {
        val sb = StringBuilder()
        val int2Hex = arrayOf("30", "31", "32", "33", "34", "35", "36", "37", "38", "39")
        for (c in stdNo.toCharArray()) {
            sb.append(int2Hex[Integer.valueOf(c.toString())])
            sb.append("00")
        }
        return sb.toString()
    }

}