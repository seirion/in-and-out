package com.trueedu.inout.ui

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.trueedu.inout.MainActivity
import com.trueedu.inout.R
import com.trueedu.inout.db.InOut
import com.trueedu.inout.db.InOutRecord
import com.trueedu.inout.db.InOutRecordBase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*


class InOutAppWidgetProvider : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.d(TAG, "onReceive")
        when (intent.action) {
            ACTION_IN -> put(InOut.IN, context)
            ACTION_OUT -> put(InOut.OUT, context)
            else -> Log.d(TAG, "wrong actions: ${intent.action}")
        }
    }

    @SuppressLint("CheckResult")
    private fun put(inOut: InOut, context: Context) {
        Log.d(TAG, "put: $inOut")
        val record = InOutRecord(inOut, Calendar.getInstance().timeInMillis)
        Single.fromCallable { InOutRecordBase.getInstance(context) }
            .subscribeOn(Schedulers.io())
            .subscribe( { it.inOutRecordDao().insert(record) }, {})

        val views = RemoteViews(context.packageName, R.layout.app_widget )
        val bgDrawable = if (inOut == InOut.IN) R.drawable.bg_round_in else R.drawable.bg_round_out
        views.setInt(R.id.widgetRoot, "setBackgroundResource", bgDrawable)

        val appWidget = ComponentName(context, InOutAppWidgetProvider::class.java)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        appWidgetManager.updateAppWidget(appWidget, views)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d(TAG, "onUpdate")
        appWidgetIds.forEach { id ->
            updateAppWidget(context, appWidgetManager, id)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
            .let { intent ->
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                PendingIntent.getActivity(context, 0, intent, 0)
            }
        val inIntent: PendingIntent = Intent(context, InOutAppWidgetProvider::class.java)
            .let { intent ->
                intent.action = ACTION_IN
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        val outIntent: PendingIntent = Intent(context, InOutAppWidgetProvider::class.java)
            .let { intent ->
                intent.action = ACTION_OUT
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }

        val views = RemoteViews(context.packageName, R.layout.app_widget )
        views.setOnClickPendingIntent(R.id.widgetRoot, pendingIntent)
        views.setOnClickPendingIntent(R.id.widgetInButton, inIntent)
        views.setOnClickPendingIntent(R.id.widgetOutButton, outIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d(TAG, "onEnabled")
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(TAG, "onDisabled")
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        Log.d(TAG, "onDeleted")
    }

    companion object {
        private val TAG = InOutAppWidgetProvider::class.java.simpleName
        const val ACTION_IN = "ACTION_BROADCAST_IN"
        const val ACTION_OUT = "ACTION_BROADCAST_OUT"
    }
}