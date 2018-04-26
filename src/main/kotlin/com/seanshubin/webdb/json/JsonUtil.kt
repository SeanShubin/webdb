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

    fun mapToJson(map: Map<String, Any>): String =
            mapper.writeValueAsString(map)

    fun listToJson(list: List<Map<String, Any>>): String =
            mapper.writeValueAsString(list)

    fun <T> fromJson(json: String, theClass: Class<T>): T =
            mapper.readValue(json, theClass)
}
