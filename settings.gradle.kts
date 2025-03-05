rootProject.name = "ecommerce"

include("auth-service")

include("product-service")
include(":product-service:product-service-domain")

include("order-service")
include(":order-service:order-service-domain")
//project(":order-service-domain").projectDir = file("order-service/order-service-domain")