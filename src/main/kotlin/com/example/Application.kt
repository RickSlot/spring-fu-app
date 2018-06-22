package com.example

import org.springframework.fu.application
import org.springframework.fu.module.data.mongodb.mongodb
import org.springframework.fu.module.jackson.jackson
import org.springframework.fu.module.webflux.netty.netty
import org.springframework.fu.module.webflux.webflux

val app = application {
    bean<UserRepository>()
    bean<UserHandler>()

    val env = System.getenv("VCAP_SERVICES")

    val mongoUri = if (env == null)  "mongodb://localhost:27017/my-new-test" else {
        val objectMapper = com.fasterxml.jackson.databind.ObjectMapper()
        val tree = objectMapper.readTree(env)
        tree.get("mlab")[0].get("credentials").get("uri").textValue()
    }

    mongodb(connectionString = mongoUri)
    webflux {
        val port = if (profiles.contains("test")) 8181 else 8080
        server(netty(port)) {
            codecs {
                jackson()
            }
            routes(ref = ::userRoutes)
        }
    }
}

fun main(args: Array<String>) = app.run(await = true)