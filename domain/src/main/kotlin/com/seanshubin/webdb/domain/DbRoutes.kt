package com.seanshubin.webdb.domain

import io.ktor.locations.Location

object DbRoutes {
    @Location("/")
    object Root

    @Location("/{namespace}")
    data class NamespaceRoute(val namespace: String)

    @Location("/{namespace}/{id}")
    data class IdRoute(val namespace: String, val id: String)
}
