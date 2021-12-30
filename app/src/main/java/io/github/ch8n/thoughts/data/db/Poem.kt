package io.github.ch8n.thoughts.data.db

import androidx.room.*
import io.github.ch8n.thoughts.utils.loremIpsum
import kotlinx.coroutines.flow.Flow
import java.util.*
import kotlin.random.Random


@Entity
data class Poem(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo val updatedAt: Long = System.currentTimeMillis(),
    @ColumnInfo val title: String,
    @ColumnInfo val content: String,
    @ColumnInfo val authorId: String,
) {
    companion object {
        val fake
            get() = Poem(
                title = loremIpsum((10..25).random()),
                content = loremIpsum((25..500).random()),
                authorId = UUID.randomUUID().toString()
            )
    }
}

@Dao
interface PoemDao {

    @Query("SELECT * FROM Poem WHERE authorId = :authorId ORDER BY updatedAt ASC")
    fun getAllPoem(authorId: String): Flow<List<Poem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPoem(poem: Poem)

    @Delete
    fun delete(poem: Poem)

    @Query("DELETE FROM Poem WHERE authorId = :authorId")
    fun deleteAllPoem(authorId: String)

}