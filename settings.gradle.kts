rootProject.name = "ecommerce"

include("service-discovery")
include("gateway-service")
include("auth-service")

// product
include("product-service")
project(":product-service").buildFileName = "build.gradle.disable"

include(":product-service:product-service-domain")

include(":order-service", ":order-service-domain", ":order-api")
project(":order-service-domain").projectDir = file("order-service/order-service-domain")
project(":order-service").projectDir = file("order-service/order-service")
project(":order-api").projectDir = file("order-service/order-api")