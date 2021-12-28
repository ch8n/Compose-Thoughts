package io.github.ch8n.thoughts.data.repository

import io.github.ch8n.thoughts.data.db.AppDatabase
import io.github.ch8n.thoughts.data.db.Author
import io.github.ch8n.thoughts.data.db.Poem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext

class AppRepo(private val appDatabase: AppDatabase) {

    suspend fun getAuthors(): Flow<List<Author>> = withContext(Dispatchers.IO) {
        return@withContext appDatabase.authorDao().getAllAuthor()
    }

    suspend fun addAuthor(author: Author) = withContext(Dispatchers.IO) {
        return@withContext appDatabase.authorDao().addAuthor(author)
    }

    suspend fun getAllPoems(author: Author) = withContext(Dispatchers.IO) {
        return@withContext appDatabase.poemDao().getAllPoem(author.uid)
    }

    suspend fun addPoem(poem: Poem) = withContext(Dispatchers.IO) {
        return@withContext appDatabase.poemDao().addPoem(poem)
    }

    suspend fun deletePoem(poem: Poem) = withContext(Dispatchers.IO) {
        return@withContext appDatabase.poemDao().delete(poem)
    }

    suspend fun deleteAllPoem(author: Author) = withContext(Dispatchers.IO) {
        return@withContext appDatabase.poemDao().deleteAllPoem(author.uid)
    }

}