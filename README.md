# Simple healthcheck library for akka-http [![Build Status](https://travis-ci.org/timeoutdigital/akka-http-healthchecks.svg?branch=master)](https://travis-ci.org/timeoutdigital/akka-http-healthchecks)

This library contains a simple health check implementation with corresponding route for akka http.
 
Health check is a function that returns cats ```ValidatedNel[String, Unit]```. Invalid value should contain reason why healthcheck failed. 
 
You can add it to your build by including these lines in your sbt file:
   
```
    resolvers += Resolver.bintrayRepo("timeoutdigital","releases")
    
    libraryDependencies += "com.timeout" %% "akka-http-healthchecks" % "1.1.1"
```   
   
Here are some examples how to use it:

```scala
import com.timeout.healthchecks._
import cats.syntax.validated._

import scala.concurrent.Future
import scala.util.Random

val simpleHealthCheck = healthCheck("luck") {
  if (Random.nextBoolean()) healthy else "Unlucky!".invalidNel
}

val simpleCheckThatReturnsAsyncResult = asyncHealthCheck("eventually lucky") {
  Future {
    if (Random.nextBoolean()) healthy else "Unlucky!".invalidNel
  }
}

val pingCheck = HealthChecks.ping("google.com", 80)
val pingCheckFromUrl = HealthChecks.pingUrl("http://www.google.com")
```

Library also contains a simple akka-http route that executes health checks and returns a page with status in json format.

Here's an example

```scala
val pingGoogle = HealthChecks.ping("www.google.com", 80)
val pingFacebook = HealthChecks.ping("www.facebook.com", 80)

val healthRoute = HealthRoutes.health(pingGoogle, pingFacebook)
Http().bindAndHandle(healthRoute, "0.0.0.0", 80)
```


