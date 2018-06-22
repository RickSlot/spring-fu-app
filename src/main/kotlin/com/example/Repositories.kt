package com.example

import org.springframework.data.mongodb.core.ReactiveMongoTemplate

class UserRepository(private val template: ReactiveMongoTemplate) {

    fun create(customer: Customer) = template.insert(customer)

    fun findById(id: String) = template.findById(id, Customer::class.java)
}