package io.sebi

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.http.content.*
import java.io.File

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
        get("/upload") {
            call.respondText("""
        <form action="/ul" enctype='multipart/form-data' method='post'>
            <input type='file' name='file'><input type='submit'>
        </form>
    """.trimIndent(),
                contentType = ContentType.Text.Html)
        }
        post("/ul") {
            call.receiveMultipart().forEachPart {
                if (it is PartData.FileItem) {
                    val targetFile = File.createTempFile("xzy", ".mp4", File("downloads").apply { mkdir() })
                    it.streamProvider().use { partstream ->
                        targetFile.outputStream().buffered().use {
                            partstream.copyTo(it)
                        }
                    }
                    println("terminated without issues or exceptions.")
                }
            }
        }
    }
}

