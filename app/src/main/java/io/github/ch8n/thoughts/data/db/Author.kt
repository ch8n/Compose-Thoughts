package io.github.ch8n.thoughts.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Entity
data class Author(
    @PrimaryKey val uid: Long = UUID.randomUUID().timestamp(),
    @ColumnInfo val name: String,
    @ColumnInfo val avatarUri: String,
)


@Dao
interface AuthorDao {
    @Query("SELECT * FROM Author")
    fun getAllAuthor(): Flow<List<Author>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAuthor(author: Author)

    @Delete
    fun delete(author: Author)
}
