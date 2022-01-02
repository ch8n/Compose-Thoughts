package io.github.ch8n.thoughts.ui.analytics


interface AppAnalytics {
    fun logEvent(
        eventName: String,
        screen: String,
        vararg attributes: Pair<String, Any>
    ): Map<String, Any>
}
