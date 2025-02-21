package nu.westlin.gradle.springgradlemultiplesourcesets.order

import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher

@Suppress("invisible_reference") // <- Because of a bug in IntelliJ (see README.md)
class DefaultOrderManagementTest {
    private val eventPublisher: ApplicationEventPublisher = mockk()

    private val orderManagement = DefaultOrderManagement(eventPublisher)

    @Test
    fun `complete order 2`() {
        val order = Order(OrderId())
        val orderCompletedEvent = OrderCompleted(order.id)
        justRun { eventPublisher.publishEvent(orderCompletedEvent) }

        orderManagement.complete(order)

        verify { eventPublisher.publishEvent(orderCompletedEvent) }
    }

}