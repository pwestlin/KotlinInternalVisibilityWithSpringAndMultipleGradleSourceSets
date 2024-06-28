package nu.westlin.gradle.springgradlemultiplesourcesets.order

import java.util.UUID

data class Order(val id: OrderId) {

    @JvmInline
    value class OrderId(val value: UUID) {}
}

fun OrderId(): Order.OrderId = Order.OrderId(UUID.randomUUID())