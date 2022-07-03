package com.lucasginard.dolarpy.widget

import android.app.PendingIntent
import android.content.Intent
import android.appwidget.AppWidgetManager
import android.widget.RemoteViews
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.net.Uri
import com.lucasginard.dolarpy.R
import android.widget.Toast

class ListWidgetProvider : AppWidgetProvider() {
    override fun onReceive(context: Context?, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == REFRESH_ACTION) {
            Toast.makeText(context,ListWidgetService.mWidgetItems.size.toString(),Toast.LENGTH_SHORT).show()
            val extras = intent.extras
            if (extras != null) {
                val appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS)
                if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
                    this.onUpdate(context!!, AppWidgetManager.getInstance(context), appWidgetIds)
                }
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for(x in appWidgetIds){
            val intent = Intent(context, ListWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, x)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            views.setRemoteAdapter(R.id.list_view, intent)
            views.setEmptyView(R.id.list_view, R.id.empty_view)
            val toastIntent = Intent(context, ListWidgetProvider::class.java)
            toastIntent.action = REFRESH_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            val toastPendingIntent = PendingIntent.getBroadcast(
                context, 0, toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setPendingIntentTemplate(R.id.list_view, toastPendingIntent)
            appWidgetManager.updateAppWidget(appWidgetIds, views)
        }
    }

    companion object {
        const val REFRESH_ACTION = "RefreshAPI"
    }
}
