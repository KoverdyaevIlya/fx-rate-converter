package rates

class FxRateContainerImpl : FxRateContainer {

    private var fxRatesMap: MutableMap<String, MutableList<FxRate>> = mutableMapOf()

    override fun add(ccyPair: String, fxRate: Double, timestamp: Long) {
        fxRatesMap.getOrPut(ccyPair) { mutableListOf() }.add(FxRate(fxRate, timestamp))
    }

    override fun get(ccyPair: String, timestamp: Long): Double? = getFxRate(ccyPair, timestamp)?.rate

    private fun getFxRate(ccyPair: String, timestamp: Long) =
        fxRatesMap[ccyPair]?.lastOrNull { it.timestamp <= timestamp }


    //just a base average
    override fun average(ccyPair: String, start: Long, end: Long): Double =
        getFxRatesInPeriod(ccyPair, start, end)
            .map { it.rate }
            .average()


    // seems it called the weighted average exchange rate
    fun average2(ccyPair: String, start: Long, end: Long): Double = getFxRatesInPeriod(ccyPair, start, end)
        .run {
            mapIndexed { i, fxRate ->
                FxRateDuration( //map every rate to its duration
                    fxRate.rate,
                    when (i) {
                        0 -> this[1].timestamp - start //for the first element we get time from start to the second element
                        this.size - 1 -> end - fxRate.timestamp //for the last element
                        else -> this[i + 1].timestamp - fxRate.timestamp
                    }
                )
            }
        }
        .sumOf { it.rate * it.duration } / (end - start)


    private fun getFxRatesInPeriod(ccyPair: String, start: Long, end: Long) =
        (fxRatesMap[ccyPair] ?: mutableListOf()).filter { it.timestamp in start..end }
            .toMutableList() //just in case if rates added to container in wrong order
        .also { if (it.isNotEmpty()) it.add(0, getFxRate(ccyPair, start) ?: FxRate(Double.NaN, start)) } //we need to add actual rate from start to the first fetched timestamp

    fun count() = fxRatesMap.flatMap { it.value }.count()
}



data class FxRateDuration(val rate: Double, val duration: Long)
