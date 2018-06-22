package com.example

import org.springframework.fu.module.webflux.server.handler
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest

class UserHandler(private val userRepository: UserRepository) {
    fun createUser(request: ServerRequest) = handler {
        request.bodyToMono(Customer::class.java).flatMap {
            userRepository.create(it).flatMap {
                ok().contentType(MediaType.APPLICATION_JSON).syncBody(it)
            }
        }
    }

    fun findUser(request: ServerRequest) = handler {
        userRepository.findById(request.pathVariable("id")).flatMap {
            ok().contentType(MediaType.APPLICATION_JSON).syncBody(it)
        }
    }

}