package com.seanshubin.webdb

import com.seanshubin.webdb.db.contract.Datum
import com.seanshubin.webdb.db.contract.Id
import com.seanshubin.webdb.db.contract.NamespaceId
import com.seanshubin.webdb.db.memory.InMemoryDatabase
import com.seanshubin.webdb.json.JsonUtil.fromJson
import com.seanshubin.webdb.json.JsonUtil.listToJson
import com.seanshubin.webdb.json.JsonUtil.mapToJson
import com.seanshubin.webdb.route.DbRoutes.IdRoute
import com.seanshubin.webdb.route.DbRoutes.NamespaceRoute
import com.seanshubin.webdb.route.DbRoutes.Root
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.locations.delete
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.pipeline.PipelineContext
import io.ktor.response.respondText
import io.ktor.routing.routing
import kotlinx.coroutines.experimental.io.ByteReadChannel
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets

fun Application.mainModule() {
    val db = InMemoryDatabase()
    val jsonMapClass: Class<Map<String, Any>> = Map::class.java as Class<Map<String, Any>>
    install(Locations)
    //TODO("get rid of this try/catch duplication")
    routing {
        get<Root> {
            try {
                respondText("OK")
            } catch (throwable: Throwable) {
                val outStream = ByteArrayOutputStream()
                val printStream = PrintStream(outStream)
                throwable.printStackTrace(printStream)
                val stackTraceAsString = String(outStream.toByteArray())
                respondText(stackTraceAsString, ContentType.Text.Plain, HttpStatusCode.BadRequest)
                throwable.printStackTrace()
            }
        }
        get<NamespaceRoute> { (namespace) ->
            try {
                val map: List<Map<String, Any>> = db.getAllInNamespace(NamespaceId(namespace)).unbox()
                respondText(listToJson(map))
            } catch (throwable: Throwable) {
                val outStream = ByteArrayOutputStream()
                val printStream = PrintStream(outStream)
                throwable.printStackTrace(printStream)
                val stackTraceAsString = String(outStream.toByteArray())
                respondText(stackTraceAsString, ContentType.Text.Plain, HttpStatusCode.BadRequest)
                throwable.printStackTrace()
            }
        }
        get<IdRoute> { (namespace, id) ->
            try {
                val datum = db[NamespaceId(namespace), Id(id)]
                respondText(mapToJson(datum.content))
            } catch (throwable: Throwable) {
                val outStream = ByteArrayOutputStream()
                val printStream = PrintStream(outStream)
                throwable.printStackTrace(printStream)
                val stackTraceAsString = String(outStream.toByteArray())
                respondText(stackTraceAsString, ContentType.Text.Plain, HttpStatusCode.BadRequest)
                throwable.printStackTrace()
            }
        }
        post<NamespaceRoute> { (namespace) ->
            try {
                val body = readBodyUtf8(call.request.receiveChannel())
                val id = db.create(NamespaceId(namespace), Datum(fromJson(body, jsonMapClass)))
                respondText(id.value)
            } catch (throwable: Throwable) {
                val outStream = ByteArrayOutputStream()
                val printStream = PrintStream(outStream)
                throwable.printStackTrace(printStream)
                val stackTraceAsString = String(outStream.toByteArray())
                respondText(stackTraceAsString, ContentType.Text.Plain, HttpStatusCode.BadRequest)
                throwable.printStackTrace()
            }
        }
        post<IdRoute> { (namespace, id) ->
            try {
                val body = readBodyUtf8(call.request.receiveChannel())
                db[NamespaceId(namespace), Id(id)] = Datum(fromJson(body, jsonMapClass))
                respondText(id)
            } catch (throwable: Throwable) {
                val outStream = ByteArrayOutputStream()
                val printStream = PrintStream(outStream)
                throwable.printStackTrace(printStream)
                val stackTraceAsString = String(outStream.toByteArray())
                respondText(stackTraceAsString, ContentType.Text.Plain, HttpStatusCode.BadRequest)
                throwable.printStackTrace()
            }
        }
        delete<IdRoute> { (namespace, id) ->
            try {
                db.delete(NamespaceId(namespace), Id(id))
                respondText(id)
            } catch (throwable: Throwable) {
                val outStream = ByteArrayOutputStream()
                val printStream = PrintStream(outStream)
                throwable.printStackTrace(printStream)
                val stackTraceAsString = String(outStream.toByteArray())
                respondText(stackTraceAsString, ContentType.Text.Plain, HttpStatusCode.BadRequest)
                throwable.printStackTrace()
            }
        }
    }

}


private suspend fun PipelineContext<Unit, ApplicationCall>.respondText(body: String) {
    respondText(body, ContentType.Text.Plain, HttpStatusCode.OK)
}

private suspend fun PipelineContext<Unit, ApplicationCall>.respondText(body: String, contentType: ContentType? = null, status: HttpStatusCode? = null) {
    call.respondText(body + "\n", contentType, status)
}

fun List<Datum>.unbox(): List<Map<String, Any>> =
        map { value -> value.content }

suspend fun readBodyUtf8(receiveChannel: ByteReadChannel): String {
    val bytes = mutableListOf<Byte>()
    while (!receiveChannel.isClosedForRead) {
        bytes.add(receiveChannel.readByte())
    }
    return String(bytes.toByteArray(), StandardCharsets.UTF_8)
}
