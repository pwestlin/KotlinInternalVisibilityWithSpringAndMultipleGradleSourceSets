package nu.westlin.gradle.springgradlemultiplesourcesets.order

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

interface OrderManagement {
    fun complete(order: Order)
}

@Service
internal class DefaultOrderManagement(
    private val eventPublisher: ApplicationEventPublisher
) : OrderManagement {

    override fun complete(order: Order) {
        eventPublisher.publishEvent(OrderCompleted(order.id))
    }
}
