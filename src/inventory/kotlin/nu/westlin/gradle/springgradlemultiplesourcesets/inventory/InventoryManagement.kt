package nu.westlin.gradle.springgradlemultiplesourcesets.inventory

import nu.westlin.gradle.springgradlemultiplesourcesets.order.Order
import nu.westlin.gradle.springgradlemultiplesourcesets.order.OrderCompleted
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toJavaDuration

interface InventoryManagement

@Service
internal class DefaultInventoryManagement(
    private val inventoryRepository: InventoryRepository
) : InventoryManagement {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @EventListener
    fun on(event: OrderCompleted) {
        val orderid = event.orderId
        logger.info("Received order completion for $orderid")

        inventoryRepository.pack(orderid)
        // Simulate busy work
        Thread.sleep(100.toDuration(DurationUnit.MILLISECONDS).toJavaDuration())

        logger.info("Finished order completion for $orderid")
    }
}

internal interface InventoryRepository {

    fun pack(orderId: Order.OrderId)
}

@Repository
internal class DefaultInventoryRepository : InventoryRepository {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun pack(orderId: Order.OrderId) {
        logger.info("Packed order with id $orderId")
    }
}