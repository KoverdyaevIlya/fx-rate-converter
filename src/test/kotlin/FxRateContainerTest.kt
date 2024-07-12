import rates.FxRateContainerImpl
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class FxRateContainerTest {

    private val fxRateContainerImpl = FxRateContainerImpl()

    @Test
    fun testAddThenGet() {
        val now = Date().time
        fxRateContainerImpl.add("USDRUB", 92.0, now)
        fxRateContainerImpl.add("USDRUB", 92.5, now + 50)
        fxRateContainerImpl.add("EURRUB", 98.0, now)
        fxRateContainerImpl.add("EURRUB", 97.0, now)  //there is no duplicate protection now

        assertEquals(fxRateContainerImpl.count(), 4)
        assertEquals(fxRateContainerImpl.get("USDRUB", now - 1), null)
        assertEquals(fxRateContainerImpl.get("USDRUB", now), 92.0)
        assertEquals(fxRateContainerImpl.get("EURRUB", now), 97.0)
        assertEquals(fxRateContainerImpl.get("AUDRUB", now), null)
        assertEquals(fxRateContainerImpl.get("USDRUB", now + 49), 92.0)
        assertEquals(fxRateContainerImpl.get("USDRUB", now + 50), 92.5)
        assertEquals(fxRateContainerImpl.get("USDRUB", now + 51), 92.5)

    }

    @Test
    fun testAverage() {

        val now = Date().time
        fxRateContainerImpl.add("USDRUB", 90.0, now)
        fxRateContainerImpl.add("USDRUB", 93.0, now + 400)
        fxRateContainerImpl.add("USDRUB", 91.0, now + 100)
        fxRateContainerImpl.add("USDRUB", 92.0, now + 200)
        fxRateContainerImpl.add("EURRUB", 98.0, now + 100)

        assertEquals(fxRateContainerImpl.count(), 5)

        val average = fxRateContainerImpl.average("USDRUB", now + 50, now + 400)
        assertEquals(average, 91.5)
        assertEquals(fxRateContainerImpl.average("USDRUB", now + 500, now + 600), Double.NaN)

        assertEquals(fxRateContainerImpl.average("EURRUB", now, now + 600),  Double.NaN)
        assertEquals(fxRateContainerImpl.average("EURRUB", now + 100, now + 600),  98.0)
        assertEquals(fxRateContainerImpl.average("EURRUB", now + 100, now),  Double.NaN)

        println("average is $average")
        val average2 = fxRateContainerImpl.average2("USDRUB", now + 50, now + 400)
        println("weighted average is $average2")



    }

}
