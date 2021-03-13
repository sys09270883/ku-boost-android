package com.konkuk.boost.persistence.rank

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.AppContract.AppEntry.RANK
import com.konkuk.boost.persistence.AppContract.AppEntry.SEMESTER
import com.konkuk.boost.persistence.AppContract.AppEntry.TOTAL
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.AppContract.AppEntry.YEAR
import java.util.*

@Entity(
    tableName = RankContract.TABLE_NAME,
    primaryKeys = [USERNAME, YEAR, SEMESTER]
)
data class RankEntity(
    @ColumnInfo(name = USERNAME)
    val username: String,
    @ColumnInfo(name = YEAR)
    val year: Int,
    @ColumnInfo(name = SEMESTER)
    val semester: Int,
    @ColumnInfo(name = RANK)
    val rank: Int,
    @ColumnInfo(name = TOTAL)
    val total: Int
) {
    override fun equals(other: Any?): Boolean {
        (other as? RankEntity)?.let {
            return (it.username == other.username) &&
                    (it.year == other.year) &&
                    (it.semester == other.semester)
        } ?: return super.equals(other)
    }

    override fun hashCode(): Int {
        return Objects.hash(this.username, this.year, this.semester)
    }

    fun toRankAndTotal(): Pair<Int, Int> {
        return Pair(this.rank, this.total)
    }
}