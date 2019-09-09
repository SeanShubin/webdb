package com.seanshubin.webdb.console

import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    val port = getPortFromArgs(args)
    embeddedServer(Netty, port = port, module = Application::mainModule).start(wait = true)
}

fun getPortFromArgs(args: Array<String>): Int =
        when (args.size) {
            0 -> 8080
            1 -> Integer.parseInt(args[0])
            else -> throw RuntimeException("expected exactly one argument, the port")
        }
