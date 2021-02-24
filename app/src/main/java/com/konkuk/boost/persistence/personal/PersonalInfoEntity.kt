package com.konkuk.boost.persistence.personal

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.personal.PersonalInfoContract.PersonalInfoEntry
import java.util.*


@Entity(
    tableName = PersonalInfoEntry.TABLE_NAME,
    primaryKeys = [PersonalInfoEntry.USERNAME, PersonalInfoEntry.KEY]
)
data class PersonalInfoEntity(
    @ColumnInfo(name = PersonalInfoEntry.USERNAME)
    val username: String,
    @ColumnInfo(name = PersonalInfoEntry.KEY)
    val key: String,
    @ColumnInfo(name = PersonalInfoEntry.VALUE)
    val value: String,
) {

    override fun equals(other: Any?): Boolean {
        (other as? PersonalInfoEntity)?.let {
            return (it.username == other.username) &&
                    (it.key == other.key)
        } ?: return super.equals(other)
    }

    override fun hashCode(): Int {
        return Objects.hash(this.username, this.key)
    }
}