package com.seanshubin.webdb.domain.db.memory

import com.seanshubin.webdb.domain.db.contract.Datum
import com.seanshubin.webdb.domain.db.contract.Id
import com.seanshubin.webdb.domain.db.contract.NamespaceId
import com.seanshubin.webdb.domain.map.MapUtil

class InMemoryNamespaceData(private val namespace: NamespaceId) {
    private var lastIdValue: Int = 0
    private val keyOrder: MutableList<Id> = mutableListOf()
    private val currentData: MutableMap<Id, Datum> = mutableMapOf()
    fun create(datum: Datum): Id {
        lastIdValue++
        val id = Id("${namespace.name}-$lastIdValue")
        keyOrder.add(id)
        currentData[id] = datum.withId(id.value)
        return id
    }

    operator fun get(id: Id): Datum = currentData[id]!!
    fun all(): List<Datum> = keyOrder.map { currentData[it]!! }
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
        keyOrder.remove(id)
        currentData.remove(id)
    }
}
