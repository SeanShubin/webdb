package com.seanshubin.webdb.domain.db.contract

import com.seanshubin.webdb.domain.map.MapUtil

data class Datum(val content: Any?) {
    fun withId(id: String): Datum = Datum(MapUtil.merge(content, mapOf(Pair("id", id))))
}
