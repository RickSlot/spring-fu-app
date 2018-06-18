package com.example

import ch.qos.logback.classic.Level.DEBUG
import ch.qos.logback.classic.Level.WARN
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.fu.application
import org.springframework.fu.module.data.mongodb.mongodb
import org.springframework.fu.module.logging.*
import org.springframework.fu.module.webflux.netty.netty
import org.springframework.fu.module.webflux.webflux
import org.springframework.fu.module.jackson.jackson
import org.springframework.fu.module.webflux.server.handler
import org.springframework.fu.ref
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest

val app = application {
	bean<UserHandler>()
	mongodb(connectionString = "mongodb://localhost:27017/reactive-test")
	logging {
		level(LogLevel.INFO)
		logback {
			consoleAppender()
		}
	}
	webflux {
		val port = if (profiles.contains("test")) 8181 else 8080
		server(netty(port)) {
			codecs {
				jackson()
			}
			routes {
				val userHandler = ref<UserHandler>()
				GET("/") { ok().syncBody("Hello world!") }
				GET("/arno") { ok().syncBody("Welkom terug Arno!") }
				GET("/user/{id}", userHandler::getUser)
				GET("/create", userHandler::createUser)
			}
		}
	}

}


@Document
data class Customer(@Id val id: String, val firstName: String)

class UserHandler(private val reactiveMongoTemplate: ReactiveMongoTemplate) {
	fun getUser(request: ServerRequest) = handler { ok().contentType(MediaType.APPLICATION_JSON).syncBody(Customer(request.pathVariable("id"), "Rick & Arno"))}
	fun createUser(request: ServerRequest) = handler {
		reactiveMongoTemplate.createCollection("RICKS")
		val saved = reactiveMongoTemplate.save(Customer("1", "test"), "customers")
		ok().build()
	}

}

fun main(args: Array<String>) = app.run(await = true)