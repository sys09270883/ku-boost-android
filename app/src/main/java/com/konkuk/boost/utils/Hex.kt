package com.konkuk.boost.utils

import okhttp3.internal.and

/**
 * Copyright 2012, Z.Alem
 * License: http://opensource.org/licenses/bsd-license.php The BSD License
 */
/**
 * Converts representation of binary data between
 * byte arrays and hexadecimal char arrays.
 *
 * @author Z. Alem <info></info>@alemcode.com>
 * Converts java to kotlin.
 * @author Yoonseop Shin
 * @since HexEditor1.0
 *
 */
object Hex {
    /**
     * Stores charaters of the hexadecimal numeral system
     */
    private val hexDigits = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    )

    /**
     * Returns hexdecimal equivalent of byte array
     *
     *
     * Creates a char array with 2:1 ratio to byte array ( 2 hex digits : 1 byte )
     * Masks (and shifts) to get value of left and right bit quartet in byte
     *
     * @param byte[] bytes
     * @return char[]    Hexadecimal char array
     */
    fun bytesToHex(bytes: ByteArray): CharArray {
        val bytesLength = bytes.size
        val hexForm = CharArray(bytesLength * 2)
        var i = 0
        var j = 0
        while (i < bytesLength) {
            hexForm[j] = hexDigits[bytes[i] and 0xf0 ushr 4]
            hexForm[j + 1] = hexDigits[bytes[i] and 0x0f]
            i++
            j = i * 2
        }
        return hexForm
    }

    /**
     * Turns a char array containing hex data into a byte array
     *
     *
     * Calculates hexadecimal pair's integer value (base 10),
     * and type casts to byte
     *
     * @param char[] hex_chars
     * @return byte[]    Hexadecimal char array
     */
    fun hexToBytes(hexChars: CharArray): ByteArray {
        val hexLength = hexChars.size
        val byteLength = hexLength / 2
        val byteForm = ByteArray(byteLength)
        var i = 0
        var j = 0
        while (i < byteLength) {
            val mostSig = hexToDec(hexChars[j], 1)
            val leastSig = hexToDec(hexChars[j + 1], 0)
            byteForm[i] = (mostSig + leastSig).toByte()
            i++
            j = i * 2
        }
        return byteForm
    }

    /**
     * Converts hex character to decimal (integer) value
     *
     * @param hex_char The hex character to convert
     * @param position Character position or distance from radix point.
     * (e.g. for C3, C is at 1 and 3 is at 0 )
     */
    private fun hexToDec(hex_char: Char, position: Int): Int {
        return Character.digit(hex_char, 16) shl position * 4
    }
}