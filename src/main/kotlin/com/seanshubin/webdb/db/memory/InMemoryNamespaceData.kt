package com.seanshubin.webdb.db.memory

import com.seanshubin.webdb.db.contract.Datum
import com.seanshubin.webdb.db.contract.Id
import com.seanshubin.webdb.db.contract.NamespaceId

class InMemoryNamespaceData(private val namespace: NamespaceId) {
    private var lastIdValue: Int = 0
    private val currentData: MutableMap<Id, Datum> = mutableMapOf()
    fun create(datum: Datum): Id {
        lastIdValue++
        val id = Id("${namespace.name}-$lastIdValue")
        currentData[id] = datum
        return id
    }

    operator fun get(id: Id): Datum = currentData[id]!!
    fun all(): Map<Id, Datum> = currentData
    operator fun set(id: Id, datum: Datum) {
        currentData[id] = datum
    }

    fun delete(id: Id) {
        currentData.remove(id)
    }
}
