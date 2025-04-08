rootProject.name = "ecommerce"

include("service-discovery")
include("gateway-service")

include(":event-core", "event-outbox-core", "mapper-core")
project(":event-core").projectDir = file("core/event-core")
project(":event-outbox-core").projectDir = file("core/event-outbox-core")
project(":mapper-core").projectDir = file("core/mapper-core")

// auth
include("auth-service")

// product
include(":product-domain")
project(":product-domain").projectDir = file("product-service/product-domain")

// payment
include("payment-domain", ":payment-infra", ":payment-app")
project(":payment-domain").projectDir = file("payment-service/payment-domain")
project(":payment-infra").projectDir = file("payment-service/payment-infra")
project(":payment-app").projectDir = file("payment-service/payment-app")

// order
include(":order-domain", ":order-infra", ":order-app")
project(":order-domain").projectDir = file("order-service/order-domain")
project(":order-infra").projectDir = file("order-service/order-infra")
project(":order-app").projectDir = file("order-service/order-app")