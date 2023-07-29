package com.example.wfmarket.models.responses.tradableItems
import com.beust.klaxon.*

data class ItemOrders (
    val payload: Orders
)

data class Orders (
    val orders: List<Order>
)

data class Order (
    val creation_date: String? = null,
    val visible: Boolean? = null,
    val quantity: Long? = null,
    val user: User,
    val last_update: String? = null,
    val platinum: Long? = null,
    val order_type: String? = null,
    val platform: String? = null,
    val id: String? = null,
    val region: String
)


data class User (
    val reputation: Long,
    val locale: String? = null,
    val avatar: String? = null,
    val ingame_name: String? = null,
    val last_seen: String? = null,
    val id: String? = null,
    val region: String? = null,
    val status: String? = null
)


