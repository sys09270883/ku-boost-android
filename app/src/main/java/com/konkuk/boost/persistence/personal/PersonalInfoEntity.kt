package com.konkuk.boost.persistence.personal

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.AppContract.AppEntry.KEY
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.AppContract.AppEntry.VALUE
import java.util.*


@Entity(
    tableName = PersonalInfoContract.TABLE_NAME,
    primaryKeys = [USERNAME, KEY]
)
data class PersonalInfoEntity(
    @ColumnInfo(name = USERNAME)
    val username: String,
    @ColumnInfo(name = KEY)
    val key: String,
    @ColumnInfo(name = VALUE)
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