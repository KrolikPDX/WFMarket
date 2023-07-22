package com.example.wfmarket.models.responses.tradableItems

data class TradableItems (
    val payload: TradableItemsPayload
)

data class TradableItemsPayload (
    var items: List<Items>
)

data class Items(
    var orders: List<Order>,
    val id:String,
    val url_name:String,
    val thumb: String,
    val item_name: String
)
