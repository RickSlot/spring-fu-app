package com.example

import org.springframework.fu.module.webflux.routes
import org.springframework.fu.module.webflux.server.handler
import org.springframework.fu.ref
import org.springframework.web.reactive.function.server.ServerRequest

fun userRoutes() = routes {
    val userHandler = ref<UserHandler>()
    GET("/users/{id}", userHandler::findUser)
    POST("/users", userHandler::createUser)
    GET("/info", infoHandler)
}

var infoHandler = fun (request: ServerRequest) = handler {



    val test = """{"newrelic":[{
  "name": "newrelic-reactive-webapp",
  "instance_name": "newrelic-reactive-webapp",
  "binding_name": null,
  "credentials": {
    "licenseKey": "4789207144c05ca2528b619179199259e5b39e56"
  },
  "syslog_drain_url": null,
  "volume_mounts": [

  ],
  "label": "newrelic",
  "provider": null,
  "plan": "standard",
  "tags": [
    "Web-based",
    "Performance Management",
    "BI & Analytics",
    "Windows",
    "Human Resources",
    "Analytics",
    "Monitoring",
    "Mac"
  ]
}],"mlab":[{
  "name": "mongo-reactive-webapp",
  "instance_name": "mongo-reactive-webapp",
  "binding_name": null,
  "credentials": {
    "uri": "mongodb://CloudFoundry_e02to619_mtn0rq4v_fhb4plpi:XTohX660-j6AXFHmar5d9ze_5zVhzhji@ds261430.mlab.com:61430/CloudFoundry_e02to619_mtn0rq4v"
  },
  "syslog_drain_url": null,
  "volume_mounts": [

  ],
  "label": "mlab",
  "provider": null,
  "plan": "sandbox",
  "tags": [
    "Cloud Databases",
    "Developer Tools",
    "Web-based",
    "Data Store",
    "document",
    "Windows",
    "Security",
    "IT Management",
    "mongodb",
    "Mac"
  ]
}]}"""



    val env = System.getenv("VCAP_SERVICES")

    val objectMapper = com.fasterxml.jackson.databind.ObjectMapper()
    val tree = objectMapper.readTree(test)

    val mlab = tree.get("mlab");
    val mongoUri = mlab[0].get("credentials").get("uri").toString();

    ok().syncBody(mongoUri)
}