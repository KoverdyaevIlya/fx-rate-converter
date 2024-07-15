package rates

class FxRateContainerListImpl : FxRateContainer {

    private var fxRates: MutableList<FxRateList> = mutableListOf()

    override fun add(ccyPair: String, fxRate: Double, timestamp: Long) {
        fxRates.add(FxRateList(ccyPair, fxRate, timestamp))
    }

    override fun get(ccyPair: String, timestamp: Long): Double? = getFxRateList(ccyPair, timestamp)?.rate

    private fun getFxRateList(ccyPair: String, timestamp: Long) = fxRates.filter { it.ccyPair == ccyPair }
        .sortedBy { it.timestamp }
        .lastOrNull { it.timestamp <= timestamp }

    //just a base average
    override fun average(ccyPair: String, start: Long, end: Long): Double =
        getFxRateListsInPeriod(ccyPair, start, end)
            .map { it.rate }
            .average()


    override fun weightedAverage(ccyPair: String, start: Long, end: Long): Double =
        getFxRateListsInPeriod(ccyPair, start, end)
            .run {
                mapIndexed { i, fxRate ->
                    FxRateListDuration( //map every rate to its duration
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


    private fun getFxRateListsInPeriod(ccyPair: String, start: Long, end: Long): MutableList<FxRateList> =
        fxRates.filter { it.ccyPair == ccyPair && it.timestamp in start..end }
            .sortedBy { it.timestamp }.toMutableList() //just in case if rates added to container in wrong order
            .also {
                if (it.isNotEmpty()) it.add(
                    0,
                    getFxRateList(ccyPair, start) ?: FxRateList(ccyPair, Double.NaN, start)
                )
            } //we need to add actual rate from start to the first fetched timestamp

    override fun count() = fxRates.count()
}


data class FxRateList(val ccyPair: String, val rate: Double, val timestamp: Long) {
}

data class FxRateListDuration(val rate: Double, val duration: Long)
