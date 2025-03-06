rootProject.name = "ecommerce"

include("auth-service")

// product
include("product-service")
project(":product-service").buildFileName = "build.gradle.disable"

include(":product-service:product-service-domain")

// order
include("order-service")
project(":order-service").buildFileName = "build.gradle.disable"

include(":order-service:order-service-domain")
include(":order-service:order-service")
//project(":order-service-domain").projectDir = file("order-service/order-service-domain")