package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.util.Log

class QuoteWidgetProvider : AppWidgetProvider() {

    companion object {
        private const val TAG = "QuoteWidgetProvider"

        private val quotes = listOf(
            "Believe in yourself! – Unknown",
            "Success is not final, failure is not fatal. – Winston Churchill",
            "You miss 100% of the shots you don't take. – Wayne Gretzky",
            "The only way to do great work is to love what you do. – Steve Jobs",
            "Life is what happens when you're busy making other plans. – John Lennon",
            "The future belongs to those who believe in the beauty of their dreams. – Eleanor Roosevelt",
            "It does not matter how slowly you go as long as you do not stop. – Confucius",
            "Everything you've ever wanted is on the other side of fear. – George Addair",
            "Success is not the key to happiness. Happiness is the key to success. – Albert Schweitzer",
            "The best time to plant a tree was 20 years ago. The second best time is now. – Chinese Proverb",
            "If you want to lift yourself up, lift up someone else. – Booker T. Washington",
            "Whether you think you can or you think you can't, you're right. – Henry Ford",
            "I have not failed. I've just found 10,000 ways that won't work. – Thomas Edison",
            "The only limit to our realization of tomorrow is our doubts of today. – Franklin D. Roosevelt",
            "Do what you can, with what you have, where you are. – Theodore Roosevelt",
            "Be yourself; everyone else is already taken. – Oscar Wilde",
            "The only impossible journey is the one you never begin. – Tony Robbins",
            "In the middle of difficulty lies opportunity. – Albert Einstein",
            "It always seems impossible until it's done. – Nelson Mandela",
            "Don't watch the clock; do what it does. Keep going. – Sam Levenson"
        )

        private fun getRandomQuote(): String {
            return quotes.random()
        }

        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            try {
                val views = RemoteViews(context.packageName, R.layout.widget_quote)

                val randomQuote = getRandomQuote()
                Log.d(TAG, "Selected quote: $randomQuote")

                val parts = randomQuote.split(" – ")
                val quoteText = parts[0]
                val author = parts.getOrNull(1) ?: "Unknown"

                views.setTextViewText(R.id.quote_text, "\"$quoteText\"")
                views.setTextViewText(R.id.quote_author, "- $author")

                val intent = Intent(context, QuoteWidgetProvider::class.java).apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
                }

                // Use FLAG_IMMUTABLE for Android 12+
                val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }

                val pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, flags)
                views.setOnClickPendingIntent(R.id.quote_text, pendingIntent)
                views.setOnClickPendingIntent(R.id.quote_author, pendingIntent)

                // Update the entire widget container to be clickable
                views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)

                appWidgetManager.updateAppWidget(appWidgetId, views)
                Log.d(TAG, "Widget updated successfully for ID: $appWidgetId")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating widget: ${e.message}", e)
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d(TAG, "onUpdate called for ${appWidgetIds.size} widgets")
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.d(TAG, "onReceive: ${intent.action}")
    }
}