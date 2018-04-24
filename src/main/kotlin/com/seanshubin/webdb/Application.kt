package com.seanshubin.webdb

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.seanshubin.webdb.db.contract.Datum
import com.seanshubin.webdb.db.contract.Id
import com.seanshubin.webdb.db.contract.NamespaceId
import com.seanshubin.webdb.db.memory.InMemoryDatabase
import com.seanshubin.webdb.route.DbRoutes.IdRoute
import com.seanshubin.webdb.route.DbRoutes.NamespaceRoute
import com.seanshubin.webdb.route.DbRoutes.Root
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.locations.Locations
import io.ktor.locations.delete
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.pipeline.PipelineContext
import io.ktor.response.respondText
import io.ktor.routing.routing
import kotlinx.coroutines.experimental.io.ByteReadChannel
import java.nio.charset.StandardCharsets

fun Application.mainModule() {
    val db = InMemoryDatabase()
    val mapper = ObjectMapper()
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
    mapper.configure(SerializationFeature.INDENT_OUTPUT, true)
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    install(Locations)
    routing {
        get<Root> {
            respondText("OK")
        }
        get<NamespaceRoute> { (namespace) ->
            val map: Map<String, String> = db.getAllInNamespace(NamespaceId(namespace)).unbox()
            respondText(mapper.writeValueAsString(map))
        }
        get<IdRoute> { (namespace, id) ->
            val datum = db[NamespaceId(namespace), Id(id)]
            respondText(datum.content)
        }
        post<NamespaceRoute> { (namespace) ->
            val body = readBodyUtf8(call.request.receiveChannel())
            val id = db.create(NamespaceId(namespace), Datum(body))
            respondText(id.value)
        }
        post<IdRoute> { (namespace, id) ->
            val body = readBodyUtf8(call.request.receiveChannel())
            db[NamespaceId(namespace), Id(id)] = Datum(body)
            respondText(id)
        }
        delete<IdRoute> { (namespace, id) ->
            db.delete(NamespaceId(namespace), Id(id))
            respondText(id)
        }
    }

}

private suspend fun PipelineContext<Unit, ApplicationCall>.respondText(body: String) {
    call.respondText(body + "\n")
}

fun Map<Id, Datum>.unbox(): Map<String, String> =
        map { (key, value) -> Pair(key.value, value.content) }.toMap()

suspend fun readBodyUtf8(receiveChannel: ByteReadChannel): String {
    val bytes = mutableListOf<Byte>()
    while (!receiveChannel.isClosedForRead) {
        bytes.add(receiveChannel.readByte())
    }
    return String(bytes.toByteArray(), StandardCharsets.UTF_8)
}
