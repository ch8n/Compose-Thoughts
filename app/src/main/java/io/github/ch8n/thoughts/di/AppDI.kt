package io.github.ch8n.thoughts.di

import android.content.Context
import io.github.ch8n.thoughts.data.db.AppDatabase
import io.github.ch8n.thoughts.data.repository.AppRepo

object AppDI {
    private lateinit var appContext: Context

    fun setAppContext(appContext: Context) {
        this.appContext = appContext
    }

    val database by lazy { AppDatabase.newInstance(appContext) }
    val appRepo by lazy { AppRepo(database) }
}