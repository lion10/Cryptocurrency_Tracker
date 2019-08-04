package com.example.cryptocurrencytracker.Adapter

import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.cryptocurrencytracker.Common.Common
import com.example.cryptocurrencytracker.Interface.IloadMore
import com.example.cryptocurrencytracker.Model.CoinModel
import com.example.cryptocurrencytracker.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coin_layout.view.*
import java.lang.StringBuilder

class CoinAdapter(recyclerView: RecyclerView,internal var activity: Activity,var items:List<CoinModel>):RecyclerView.Adapter<coinViewHolder>(){

  internal var loadMore : IloadMore?= null
    var isLoading :Boolean =false
    var visibleThreshold = 5
    var lastVisibleItem :Int = 0
    var totalItemCount: Int = 0

    init{
        val linearLayout = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayout.itemCount
                lastVisibleItem =linearLayout.findLastVisibleItemPosition()

                if (!isLoading && totalItemCount <= lastVisibleItem+visibleThreshold){
                    if( loadMore != null)
                        loadMore!!.onLoadMore()
                    isLoading = true
                }

            }
        })
    }

    fun setLoadMore(loadMore :IloadMore){
        this.loadMore = loadMore
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): coinViewHolder {
        val  view =LayoutInflater.from(activity).inflate(R.layout.coin_layout,p0,false)

        return coinViewHolder(view)


    }

    override fun getItemCount(): Int {
           return items.size
    }

    override fun onBindViewHolder(p0: coinViewHolder, p1: Int) {
        val coinModel = items.get(p1)
        val item = p0 as coinViewHolder

        item.coinName.text = coinModel.name
        item.coinSymbol.text = coinModel.symbol
        item.coinPrice.text = coinModel.price_usd
        item.oneHourChange.text = coinModel.percent_change_1h + "%"
        item.twentyFourChange.text = coinModel.percent_change_24h + "%"
        item.sevenDayChange.text = coinModel.percent_change_7d + "%"



        Picasso.with(activity.baseContext).
            load(StringBuilder(Common.imageUrl)
                .append(coinModel.symbol!!.toLowerCase())
                .append(".png").toString()).into(item.coinIcon)


        item.oneHourChange.setTextColor(
            if(coinModel.percent_change_1h!!.contains("-"))
                    Color.parseColor("#FF0000")
            else
                Color.parseColor("#32CD32")
        )
        item.twentyFourChange.setTextColor(
            if(coinModel.percent_change_24h!!.contains("-"))
                Color.parseColor("#FF0000")
            else
                Color.parseColor("#32CD32")
        )

        item.sevenDayChange.setTextColor(
            if(coinModel.percent_change_7d!!.contains("-"))
                Color.parseColor("#FF0000")
            else
                Color.parseColor("#32CD32")
        )

    }

    fun setLoaded(){
        isLoading = false
    }

    fun updateData (coinModels :List<CoinModel>){
        this.items = coinModels
        notifyDataSetChanged()
    }

}



class coinViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    var coinIcon = itemView.coinIcon
    var coinSymbol  = itemView.coinSymbol
    var coinName = itemView.coin_name
    var coinPrice = itemView.priceUSD
    var oneHourChange =  itemView.oneHour
    var twentyFourChange  =itemView.twentyFourHour
    var sevenDayChange =itemView.sevenDay



}
