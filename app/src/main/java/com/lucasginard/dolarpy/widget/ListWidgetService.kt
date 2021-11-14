package com.lucasginard.dolarpy.widget

import android.widget.RemoteViews
import android.content.Intent
import android.os.Bundle
import android.appwidget.AppWidgetManager
import android.content.Context
import android.view.View
import android.widget.RemoteViewsService
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
                    response.body()!!.dolarpy.amambay.name = "AMANBAY"
                    response.body()!!.dolarpy.bcp.name = "BCP"
                    response.body()!!.dolarpy.bonanza.name = "BONANZA"
                    response.body()!!.dolarpy.cambiosalberdi.name = "CAMBIOS ALBERDI"
                    response.body()!!.dolarpy.cambioschaco.name = "CAMBIOS CHACO"
                    response.body()!!.dolarpy.interfisa.name = "INTERFISA"
                    response.body()!!.dolarpy.lamoneda.name = "LA MONEDA"
                    response.body()!!.dolarpy.maxicambios.name = "MAXICAMBIOS"
                    response.body()!!.dolarpy.mundialcambios.name = "MUNDIAL CAMBIOS"
                    response.body()!!.dolarpy.mydcambios.name = "MYD CAMBIOS"
                    response.body()!!.dolarpy.set.name = "SET"
                    response.body()!!.dolarpy.vision.name = "Visión Banco"
                    response.body()!!.dolarpy.gnbfusion.name = "GNB FUSIÓN"
                    response.body()!!.dolarpy.eurocambios.name = "Euro Cambios"
                    list.add(response.body()!!.dolarpy.amambay)
                    list.add(response.body()!!.dolarpy.bcp)
                    list.add(response.body()!!.dolarpy.bonanza)
                    list.add(response.body()!!.dolarpy.cambiosalberdi)
                    list.add(response.body()!!.dolarpy.cambioschaco)
                    list.add(response.body()!!.dolarpy.interfisa)
                    list.add(response.body()!!.dolarpy.gnbfusion)
                    list.add(response.body()!!.dolarpy.lamoneda)
                    list.add(response.body()!!.dolarpy.maxicambios)
                    list.add(response.body()!!.dolarpy.mundialcambios)
                    list.add(response.body()!!.dolarpy.mydcambios)
                    list.add(response.body()!!.dolarpy.set)
                    list.add(response.body()!!.dolarpy.vision)
                    list.add(response.body()!!.dolarpy.eurocambios)
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
