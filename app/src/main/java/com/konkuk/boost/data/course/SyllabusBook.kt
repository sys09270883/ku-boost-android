package com.konkuk.boost.data.course

import com.google.gson.annotations.SerializedName

data class SyllabusBook(
    @SerializedName("BOOK_NM") val bookName: String,
    @SerializedName("AUTH") val author: String,
    @SerializedName("PUBL_CO") val publishedCompany: String,
    @SerializedName("BOOK_DIV") val bookDiv: String?,            // B23231: 주교재, B23232: 부교재, B23233: 참고문헌
)
