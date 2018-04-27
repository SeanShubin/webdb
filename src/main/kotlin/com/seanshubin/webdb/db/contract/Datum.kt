package com.seanshubin.webdb.db.contract

import com.seanshubin.webdb.map.MapUtil

data class Datum(val content: Any?) {
    fun withId(id: String): Datum = Datum(MapUtil.merge(content, Pair("id", id)))
    fun id(): String? = when (content) {
        is Map<*, *> -> content["id"] as String
        else -> null
    }
}
