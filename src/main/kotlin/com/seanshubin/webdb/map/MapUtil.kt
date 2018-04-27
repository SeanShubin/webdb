package com.seanshubin.webdb.map

object MapUtil {
    fun merge(aObject: Any?, bObject: Any?): Any? {
        return when {
            aObject is Map<*, *> && bObject is Map<*, *> -> mergeMaps(aObject, bObject)
            else -> bObject
        }
    }

    fun mergeMaps(aMap: Map<*, *>, bMap: Map<*, *>): Map<*, *> {
        return bMap.entries.fold(aMap, { a, b -> mergeEntry(a, b) })
    }

    fun mergeEntry(aMap: Map<*, *>, bEntry: Map.Entry<*, *>): Map<*, *> {
        val (bKey, bValue) = bEntry
        return if (bValue == null) {
            aMap - bKey
        } else {
            val aValue = aMap[bKey]
            val cValue = when (aValue) {
                null -> bValue
                else -> merge(aValue, bValue)
            }
            aMap + Pair(bKey, cValue)
        }
    }
}
