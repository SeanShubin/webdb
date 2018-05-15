package com.seanshubin.webdb.db.memory

import com.seanshubin.webdb.db.contract.Database
import com.seanshubin.webdb.db.contract.Datum
import com.seanshubin.webdb.db.contract.Id
import com.seanshubin.webdb.db.contract.NamespaceId

class InMemoryDatabase() : Database {
    private val namespaces: MutableMap<NamespaceId, InMemoryNamespaceData> = mutableMapOf()
    override fun create(namespace: NamespaceId, datum: Datum): Id = synchronized(this) {
        getOrCreateNamespaceData(namespace).create(datum)
    }

    override fun get(namespace: NamespaceId, id: Id): Datum = synchronized(this) {
        getOrCreateNamespaceData(namespace)[id]
    }

    override fun getAllInNamespace(namespace: NamespaceId): List<Datum> = synchronized(this) {
        getOrCreateNamespaceData(namespace).all()
    }

    override fun set(namespace: NamespaceId, id: Id, datum: Datum) = synchronized(this) {
        getOrCreateNamespaceData(namespace)[id] = datum
    }

    override fun delete(namespace: NamespaceId, id: Id) = synchronized(this) {
        getOrCreateNamespaceData(namespace).delete(id)
    }

    private fun getOrCreateNamespaceData(namespace: NamespaceId): InMemoryNamespaceData {
        val maybeValue = namespaces[namespace]
        return if (maybeValue == null) {
            val value = InMemoryNamespaceData(namespace)
            namespaces[namespace] = value
            value
        } else {
            maybeValue
        }
    }
}
