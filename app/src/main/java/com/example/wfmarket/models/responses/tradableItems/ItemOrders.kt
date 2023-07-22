package com.example.wfmarket.models.responses.tradableItems
import com.beust.klaxon.*

data class ItemOrders (
    val payload: Orders
)

data class Orders (
    val orders: List<Order>
)

data class Order (
    @Json(name = "creation_date")
    val creationDate: String,

    val visible: Boolean,
    val quantity: Long,
    val user: User,

    @Json(name = "last_update")
    val lastUpdate: String,

    val platinum: Long,

    @Json(name = "order_type")
    val orderType: OrderType,

    val platform: Platform,
    val id: String,
    val region: Region
)

enum class OrderType(val value: String) {
    Buy("buy"),
    Sell("sell");

    companion object {
        public fun fromValue(value: String): OrderType = when (value) {
            "buy"  -> Buy
            "sell" -> Sell
            else   -> throw IllegalArgumentException()
        }
    }
}

enum class Platform(val value: String) {
    PC("pc");

    companion object {
        public fun fromValue(value: String): Platform = when (value) {
            "pc" -> PC
            else -> throw IllegalArgumentException()
        }
    }
}

enum class Region(val value: String) {
    De("de"),
    En("en"),
    Ko("ko"),
    Pt("pt"),
    Ru("ru"),
    Sv("sv"),
    ZhHans("zh-hans");

    companion object {
        public fun fromValue(value: String): Region = when (value) {
            "de"      -> De
            "en"      -> En
            "ko"      -> Ko
            "pt"      -> Pt
            "ru"      -> Ru
            "sv"      -> Sv
            "zh-hans" -> ZhHans
            else      -> throw IllegalArgumentException()
        }
    }
}

data class User (
    val reputation: Long,
    val locale: Region,
    val avatar: String? = null,

    @Json(name = "ingame_name")
    val ingameName: String,

    @Json(name = "last_seen")
    val lastSeen: String,

    val id: String,
    val region: Region,
    val status: String
)


