package com.corgaxm.ku_alarmy.data.grade

import android.os.Parcel
import android.os.Parcelable

data class ParcelableGrade(
    val evaluationMethod: String?,
    val year: Int,
    val semester: Int,
    val classification: String?,
    val characterGrade: String?,
    val grade: Float,
    val professor: String?,
    val subjectId: String?,
    val subjectName: String?,
    val subjectNumber: String?,
    val subjectPoint: Int,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(evaluationMethod)
        parcel.writeInt(year)
        parcel.writeInt(semester)
        parcel.writeString(classification)
        parcel.writeString(characterGrade)
        parcel.writeFloat(grade)
        parcel.writeString(professor)
        parcel.writeString(subjectId)
        parcel.writeString(subjectName)
        parcel.writeString(subjectNumber)
        parcel.writeInt(subjectPoint)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableGrade> {
        override fun createFromParcel(parcel: Parcel): ParcelableGrade {
            return ParcelableGrade(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableGrade?> {
            return arrayOfNulls(size)
        }
    }

}