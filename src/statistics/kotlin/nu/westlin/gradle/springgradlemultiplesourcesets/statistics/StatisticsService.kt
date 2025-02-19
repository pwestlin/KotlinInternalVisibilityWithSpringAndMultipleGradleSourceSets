package nu.westlin.gradle.springgradlemultiplesourcesets.statistics

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Service
class StatisticsService {

    @Autowired
    private lateinit var statisticsRepository: StatisticsRepository

    fun all(): List<Statistics> = statisticsRepository.all()
}

@Component
internal class StatisticsHandler(private val statisticsRepository: StatisticsRepository) {

    @EventListener
    fun on(statistics: Statistics) {
        statisticsRepository.save(statistics)
    }
}

@Repository
internal class StatisticsRepository {

    private val list: MutableList<Statistics> = ArrayList()

    fun save(statistics: Statistics) {
        list.add(statistics)
    }

    fun all(): List<Statistics> = list

}