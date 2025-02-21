package nu.westlin.gradle.springgradlemultiplesourcesets.inventory

import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import nu.westlin.gradle.springgradlemultiplesourcesets.order.OrderCompleted
import nu.westlin.gradle.springgradlemultiplesourcesets.order.OrderId
import org.junit.jupiter.api.Test

@Suppress("invisible_reference") // <- Because of a bug in IntelliJ (see README.md)
class DefaultInventoryManagementTest {

    private val inventoryRepository: InventoryRepository = mockk()

    private val inventoryManagement = DefaultInventoryManagement(inventoryRepository)

    @Test
    fun `process complete order event`() {
        val event = OrderCompleted(OrderId())

        justRun { inventoryRepository.pack(event.orderId) }

        inventoryManagement.on(event)

        verify {
            inventoryRepository.pack(event.orderId)
        }

    }
}