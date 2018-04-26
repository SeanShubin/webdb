package com.seanshubin.webdb.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

object JsonUtil {
    private val mapper = ObjectMapper()

    init {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true)
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }

    fun <T> toJson(theObject: T): String =
            mapper.writeValueAsString(theObject)

    fun mapToJson(map: Map<String, Any>): String =
            mapper.writeValueAsString(map)

    fun listToJson(list: List<Map<String, Any>>): String =
            mapper.writeValueAsString(list)

    fun <T> fromJson(json: String, theClass: Class<T>): T =
            mapper.readValue(json, theClass)

    fun normalize(json: String): String {
        val asObject = fromJson(json, Object::class.java)
        val normalized = toJson(asObject)
        return normalized
    }

    fun mergeJsonStrings(a: String, b: String): String {
        val aObject = JsonUtil.fromJson(a, Object::class.java)
        val bObject = JsonUtil.fromJson(b, Object::class.java)
        val cObject = JsonUtil.merge(aObject, bObject)
        return toJson(cObject)
    }

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
