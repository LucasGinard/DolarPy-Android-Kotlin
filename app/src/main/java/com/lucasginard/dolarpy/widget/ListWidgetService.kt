package com.lucasginard.dolarpy.widget

import android.widget.RemoteViews
import android.content.Intent
import android.os.Bundle
import android.appwidget.AppWidgetManager
import android.content.Context
import android.view.View
import android.widget.RemoteViewsService
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.data.model.com_ven
import com.lucasginard.dolarpy.data.apiService
import com.lucasginard.dolarpy.data.model.dolarpyResponse
import com.lucasginard.dolarpy.utils.conversionToDecimal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return ListRemoteViewsFactory(applicationContext, intent)
    }

    internal inner class ListRemoteViewsFactory(context: Context, intent: Intent) :
        RemoteViewsFactory {
        private val mContext: Context
        private val mAppWidgetId: Int

        override fun onCreate() {
            mWidgetItems.clear()
            onDataSetChanged()
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        override fun onDestroy() {
            mWidgetItems.clear()
        }

        override fun getCount(): Int {
            return 13
        }



        override fun getViewAt(position: Int): RemoteViews {
            val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
            if (position > 0) {
                rv.setViewVisibility(R.id.linear_title, View.GONE)
            } else {
                rv.setViewVisibility(R.id.linear_title, View.VISIBLE)
            }
            if (updateLast != ""){
                rv.setTextViewText(R.id.update_date, "${getString(R.string.lastUpdate)}\n$updateLast")
            }
            if(mWidgetItems.size == 0){
                onDataSetChanged()
            }else{
                if(mWidgetItems.size > position){
                    rv.setTextViewText(R.id.widget_item, mWidgetItems[position].title)
                    rv.setTextViewText(R.id.widget_buy, mWidgetItems[position].buy)
                    rv.setTextViewText(R.id.widget_sell, mWidgetItems[position].sell)
                }
            }
            val extras = Bundle()
            extras.putInt(ListWidgetProvider.REFRESH_ACTION, 0)
            val fillInIntent = Intent()
            fillInIntent.putExtras(extras)
            rv.setOnClickFillInIntent(R.id.ivRefresh, fillInIntent)
            try {
                Thread.sleep(200)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            return rv
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        override fun onDataSetChanged() {
            serviceList()
        }

        init {
            mContext = context
            mAppWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
    }

    companion object {
        var list = ArrayList<com_ven>()
        var updateLast = ""
        val mWidgetItems: MutableList<WidgetItem> = ArrayList()
    }

    fun serviceList() {
        val call = apiService.getInstance().getDolar()
        mWidgetItems.clear()
        call.enqueue(object : Callback<dolarpyResponse> {
            override fun onResponse(
                call: Call<dolarpyResponse>,
                response: Response<dolarpyResponse>
            ) {
                if(mWidgetItems.isEmpty()){
                    list.clear()
                    val json = response.body()?.dolarpy
                    if (json != null){
                        val entrySet: Set<Map.Entry<String, JsonElement?>> = json.entrySet()
                        for ((key) in entrySet) {
                            val gson = Gson().fromJson(json.get(key),com_ven::class.java)
                            gson.name = key
                            list.add(gson)
                        }
                    }
                    updateLast = response.body()!!.update
                    val aux = list.sortedBy{it.compra}
                    for (x in aux){
                        if (x.compra != 0.0 && x.venta != 0.0){
                            mWidgetItems.add(WidgetItem(x.name ?: "", x.compra.conversionToDecimal(), x.venta.conversionToDecimal()))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<dolarpyResponse>, t: Throwable) {
                if (mWidgetItems.isEmpty()){
                    list.clear()
                    mWidgetItems.clear()
                    mWidgetItems.add(WidgetItem(getString(R.string.textErrorNet)))
                }
            }
        })
    }
}
