package com.seanshubin.webdb.domain.db.contract

interface Database {
    fun create(namespace: NamespaceId, datum: Datum): Id
    operator fun get(namespace: NamespaceId, id: Id): Datum
    fun getAllInNamespace(namespace: NamespaceId): List<Datum>
    operator fun set(namespace: NamespaceId, id: Id, datum: Datum)
    fun delete(namespace: NamespaceId, id: Id)
}
