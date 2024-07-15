package rates

//import benchmark.FxRate
//import org.openjdk.jmh.annotations.*
//import java.util.*
//import java.util.concurrent.TimeUnit
//
//@BenchmarkMode(Mode.AverageTime)
//@OutputTimeUnit(TimeUnit.MICROSECONDS)
//@Warmup(iterations = 10)
//class ArrayListBenchmark {
//
//
//    @State(Scope.Thread)
//    public class InitialState {
//        var employeeList: MutableList<benchmark.FxRate> = ArrayList()
//        var iterations: Long = 100000
//        var employee: benchmark.FxRate = benchmark.FxRate(90.0, 1L)
//        var employeeIndex = -1
//
//        @Setup(Level.Trial)
//        fun setUp() {
//            for (i in 0 until iterations) {
//                employeeList.add(benchmark.FxRate(92.0, 2L))
//            }
//            employeeList.add(employee)
//            employeeIndex = employeeList.indexOf(employee)
//        }
//
//    }
//
//
//    @Benchmark
//    fun testAdd(state: InitialState) {
//        state.employeeList.add(benchmark.FxRate((state.iterations + 1).toDouble(), Date().time))
//    }
//
//    @Benchmark
//    fun testAddAt(state: InitialState) {
//        state.employeeList.add(state.iterations.toInt(), benchmark.FxRate(state.iterations.toDouble(), Date().time))
//    }
//
//    @Benchmark
//    fun testContains(state: InitialState): Boolean {
//        return state.employeeList.contains(state.employee)
//    }
//
//    @Benchmark
//    fun testIndexOf(state: InitialState): Int {
//        return state.employeeList.indexOf(state.employee)
//    }
//
//    @Benchmark
//    fun testGet(state: InitialState): benchmark.FxRate? {
//        return state.employeeList[state.employeeIndex]
//    }
//
//    @Benchmark
//    fun testRemove(state: InitialState): Boolean {
//        return state.employeeList.remove(state.employee)
//    }
//}
