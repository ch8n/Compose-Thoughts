package io.github.ch8n.thoughts.utils

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashReportingTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (priority == Log.ERROR) {
            val crashlytics = FirebaseCrashlytics.getInstance()
            crashlytics.log(throwable?.stackTraceToString() ?: message)
        }
    }
}