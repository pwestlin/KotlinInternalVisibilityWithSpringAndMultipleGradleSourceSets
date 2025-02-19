package nu.westlin.gradle.springgradlemultiplesourcesets

import nu.westlin.gradle.springgradlemultiplesourcesets.order.Order
import nu.westlin.gradle.springgradlemultiplesourcesets.order.OrderId
import nu.westlin.gradle.springgradlemultiplesourcesets.order.OrderManagement
import org.springframework.beans.factory.getBean
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication
class Application(private val applicationContext: ApplicationContext) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        applicationContext.getBean<OrderManagement>().complete(Order(OrderId()))
    }

}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
