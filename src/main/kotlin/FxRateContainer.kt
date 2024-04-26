interface FxRateContainer {
    /**
     * Adds Fx Rate for specified currency pair and timestamp to the container
     */
    fun add(ccyPair: String, fxRate: Double, timestamp: Long)
    /**
     * Returns Fx Rate for currency pair at the moment of time that nearest less or equal to specified timestamp
     */
    fun get(ccyPair: String, timestamp: Long): Double?

    /**
     * Returns average Fx Rate in period from start to end inclusively
     */
    fun average(ccyPair: String, start: Long, end: Long): Double
}
