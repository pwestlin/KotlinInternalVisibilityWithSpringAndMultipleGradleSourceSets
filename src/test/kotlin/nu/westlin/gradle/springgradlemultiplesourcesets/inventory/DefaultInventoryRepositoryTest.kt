package nu.westlin.gradle.springgradlemultiplesourcesets.inventory

import nu.westlin.gradle.springgradlemultiplesourcesets.order.OrderId
import org.junit.jupiter.api.Test

@Suppress("invisible_reference", "invisible_member")
class DefaultInventoryRepositoryTest {

    private val repository = DefaultInventoryRepository()
    
    @Test
    fun `pack order`() {
        repository.pack(OrderId())
    }
}