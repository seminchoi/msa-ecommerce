rootProject.name = "ecommerce"

include("service-discovery")
include("gateway-service")

include(":event-core", "event-outbox-core")
project(":event-core").projectDir = file("core/event-core")
project(":event-outbox-core").projectDir = file("core/event-outbox-core")

// auth
include("auth")

// product
include(":product-domain")
project(":product-domain").projectDir = file("product/product-domain")

// payment
include(":payment-infra", ":payment-app")
project(":payment-app").projectDir = file("payment-service/payment-app")
project(":payment-infra").projectDir = file("payment-service/payment-infra")

// order
include(":order-service", ":order-domain", ":order-api", "order-scheduler-app")
project(":order-domain").projectDir = file("order/order-domain")
project(":order-service").projectDir = file("order/order-service")
project(":order-api").projectDir = file("order/order-api")