package com.seanshubin.webdb.db.memory

import com.seanshubin.webdb.db.contract.Datum
import com.seanshubin.webdb.db.contract.Id
import com.seanshubin.webdb.db.contract.NamespaceId
import com.seanshubin.webdb.map.MapUtil

class InMemoryNamespaceData(private val namespace: NamespaceId) {
    private var lastIdValue: Int = 0
    private val currentData: MutableMap<Id, Datum> = mutableMapOf()
    fun create(datum: Datum): Id {
        lastIdValue++
        val id = Id("${namespace.name}-$lastIdValue")
        currentData[id] = datum.withId(id.value)
        return id
    }

    operator fun get(id: Id): Datum = currentData[id]!!
    fun all(): List<Datum> = currentData.keys.sortedWith(compareBy({ it.value })).map { currentData[it]!! }
    operator fun set(id: Id, datum: Datum) {
        val oldValue = currentData[id]
        val newValue = if (oldValue == null) {
            datum.withId(id.value)
        } else {
            Datum(MapUtil.merge(oldValue.content, datum.content))
        }
        currentData[id] = newValue
    }

    fun delete(id: Id) {
        currentData.remove(id)
    }
}
