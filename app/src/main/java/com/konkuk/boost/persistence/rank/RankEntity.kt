package com.konkuk.boost.persistence.rank

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.rank.RankContract.RankEntry
import java.util.*

@Entity(
    tableName = RankEntry.TABLE_NAME,
    primaryKeys = [RankEntry.USERNAME, RankEntry.YEAR, RankEntry.SEMESTER]
)
data class RankEntity(
    @ColumnInfo(name = RankEntry.USERNAME)
    val username: String,
    @ColumnInfo(name = RankEntry.YEAR)
    val year: Int,
    @ColumnInfo(name = RankEntry.SEMESTER)
    val semester: Int,
    @ColumnInfo(name = RankEntry.RANK)
    val rank: Int,
    @ColumnInfo(name = RankEntry.TOTAL)
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