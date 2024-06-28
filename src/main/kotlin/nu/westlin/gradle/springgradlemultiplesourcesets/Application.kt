package nu.westlin.gradle.springgradlemultiplesourcesets

import nu.westlin.gradle.springgradlemultiplesourcesets.order.Order
import nu.westlin.gradle.springgradlemultiplesourcesets.order.OrderId
import nu.westlin.gradle.springgradlemultiplesourcesets.order.OrderManagement
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    val ctx = runApplication<Application>(*args)

    ctx.getBean<OrderManagement>().complete(Order(OrderId()))
}
