package io.github.ch8n.thoughts.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*


@Entity
data class Poem(
    @PrimaryKey val uid: Long = UUID.randomUUID().timestamp(),
    @ColumnInfo val updatedAt: Long = System.currentTimeMillis(),
    @ColumnInfo val content: String,
    @ColumnInfo val authorId: Long,
)

@Dao
interface PoemDao {

    @Query("SELECT * FROM Poem WHERE authorId = :authorId ORDER BY updatedAt ASC")
    fun getAllPoem(authorId: Long): Flow<List<Poem>>

    @Insert
    fun addPoem(poem: Poem)

    @Delete
    fun delete(poem: Poem)

    @Query("DELETE FROM Poem WHERE authorId = :authorId")
    fun deleteAllPoem(authorId: Long)

}