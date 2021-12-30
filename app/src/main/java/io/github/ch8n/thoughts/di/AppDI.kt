package io.github.ch8n.thoughts.di

import android.content.Context
import io.github.ch8n.thoughts.data.db.AppDatabase
import io.github.ch8n.thoughts.data.repository.AppRepo
import io.github.ch8n.thoughts.ui.screen.home.SharedViewModel

object AppDI {
    private lateinit var appContext: Context

    fun setAppContext(appContext: Context) {
        this.appContext = appContext
    }

    private val database by lazy { AppDatabase.newInstance(appContext) }
    private val appRepo by lazy { AppRepo(database) }

    val sharedViewModel by lazy { SharedViewModel(appRepo) }
}