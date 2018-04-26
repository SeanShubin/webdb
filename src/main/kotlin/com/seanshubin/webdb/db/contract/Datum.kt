package com.seanshubin.webdb.db.contract

data class Datum(val content: Map<String, Any>) {
    fun withId(id: String): Datum = Datum(content + Pair("id", id))
}
