package io.github.ch8n.thoughts.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Author::class, Poem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authorDao(): AuthorDao
    abstract fun poemDao(): PoemDao

    companion object {
        fun newInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "database-thought"
            ).build()
        }
    }
}