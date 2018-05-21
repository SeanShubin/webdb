package com.seanshubin.webdb.db.contract

import com.seanshubin.webdb.map.MapUtil

data class Datum(val content: Any?) {
    fun withId(id: String): Datum = Datum(MapUtil.merge(content, mapOf(Pair("id", id))))
}
