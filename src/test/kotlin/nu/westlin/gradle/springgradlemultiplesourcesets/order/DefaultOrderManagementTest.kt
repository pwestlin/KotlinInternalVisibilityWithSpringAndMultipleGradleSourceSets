package nu.westlin.gradle.springgradlemultiplesourcesets.order

import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher

// IntelliJ has a bug so that it doesn't recognize
// kotlin.target.compilations.getByName("test").associateWith(kotlin.target.compilations.getByName(orderSrcSet))
// from build.gradle.kts :/
@Suppress("invisible_reference", "invisible_member")
class DefaultOrderManagementTest {

    private val eventPublisher: ApplicationEventPublisher = mockk()

    private val orderManagement = DefaultOrderManagement(eventPublisher)

    @Test
    fun `complete order`() {
        val order = Order(OrderId())
        val orderCompletedEvent = OrderCompleted(order.id)
        justRun { eventPublisher.publishEvent(orderCompletedEvent) }

        orderManagement.complete(order)

        verify { eventPublisher.publishEvent(orderCompletedEvent) }
    }
}
