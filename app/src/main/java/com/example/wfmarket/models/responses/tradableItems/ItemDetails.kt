package com.example.wfmarket.models.responses.tradableItems

import com.beust.klaxon.Json

data class ItemDetails (
    val payload: ItemDetailsPayload
)

data class ItemDetailsPayload (
    val item: ItemDetailsItem
)

data class ItemDetailsItem (
    val id: String,
    val items_in_set: List<ItemsInSet>
)

data class ItemsInSet (
    val icon: String,
    val tags: List<String>,
    val thumb: String,
    val mod_max_rank: Long,
    val trading_tax: Long,
    val url_name: String,
    val rarity: String,
    val icon_format: String,
    val sub_icon: Any? = null,
    val id: String,
    val en: CS,
    /*
    val ru: com.example.wfmarket.models.responses.tradableItems.CS,
    val ko: com.example.wfmarket.models.responses.tradableItems.CS,
    val fr: com.example.wfmarket.models.responses.tradableItems.CS,
    val sv: com.example.wfmarket.models.responses.tradableItems.CS,
    val de: com.example.wfmarket.models.responses.tradableItems.CS,

    @Json(name = "zh-hant")
    val zhHant: com.example.wfmarket.models.responses.tradableItems.CS,

    @Json(name = "zh-hans")
    val zhHans: com.example.wfmarket.models.responses.tradableItems.CS,

    val pt: com.example.wfmarket.models.responses.tradableItems.CS,
    val es: com.example.wfmarket.models.responses.tradableItems.CS,
    val pl: com.example.wfmarket.models.responses.tradableItems.CS,
    val cs: com.example.wfmarket.models.responses.tradableItems.CS */
)

data class CS (
    val item_name: String,
    val description: String,
    val wiki_link: String,
    val drop: List<Any?>
)

