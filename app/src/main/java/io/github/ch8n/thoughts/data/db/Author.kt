package io.github.ch8n.thoughts.data.db

import androidx.room.*
import io.github.ch8n.thoughts.R
import kotlinx.coroutines.flow.Flow
import java.util.*

@Entity
data class Author(
    @PrimaryKey val uid: String,
    @ColumnInfo val name: String,
    @ColumnInfo val avatarUri: String,
) {

    @Ignore val placeholder: Int = R.drawable.ic_avatar

    companion object {
        val fake
            get() = Author(
                uid = "1",
                name = "Pooja Srivs",
                avatarUri = "https://cdn2.iconfinder.com/data/icons/circle-avatars-1/128/050_girl_avatar_profile_woman_suit_student_officer-512.png",
            )

        val Default = Author(
            uid = "1",
            name = "",
            avatarUri = "",
        )
    }
}


@Dao
interface AuthorDao {
    @Query("SELECT * FROM Author")
    fun getAllAuthor(): Flow<List<Author>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAuthor(author: Author)

    @Delete
    fun delete(author: Author)
}
