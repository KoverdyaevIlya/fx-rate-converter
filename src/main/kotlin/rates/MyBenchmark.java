package rates;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 10)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class MyBenchmark {

    private static double generateFxRate() {
        double leftLimit = 80D;
        double rightLimit = 95D;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private static boolean isXorEven(int x) {
        return (x ^ 1) > x;
    }

    private static String calcCcyPair(int x) {
        return x % 3 == 0 ? "USDEUR" : x % 2 == 0 ? "USDRUB" : "EURRUB";
    }

//    @Benchmark
//    public void testAddMap(InitialState state) {
//        state.fxRateContainer.add("USDRUB", generateFxRate(), Instant.now().toEpochMilli());
//    }

    //    @Benchmark
//    public void testAddList(InitialState state) {
//        state.fxRateListContainer.add("USDRUB", generateFxRate(), Instant.now().toEpochMilli());
//    }

    @Benchmark
    public Double testGetMap(InitialState state) {
        return state.fxRateContainer.get("USDRUB", state.end);
    }

    @Benchmark
    public Double testAverageMap(InitialState state) {
        return state.fxRateContainer.average("EURRUB", state.begin + (state.end - state.begin) / 2, state.end);
    }

    @Benchmark
    public Double testWeightedAverageMap(InitialState state) {
        return state.fxRateContainer.weightedAverage("USDRUB", state.begin + (state.end - state.begin) / 2, state.end);
    }

    @Benchmark
    public Double testGetList(InitialState state) {
        return state.fxRateListContainer.get("USDRUB", state.end);
    }

    @Benchmark
    public Double testAverageList(InitialState state) {
        return state.fxRateListContainer.average("EURRUB", state.begin + (state.end - state.begin) / 2, state.end);
    }

    @Benchmark
    public Double testWeightedAverageList(InitialState state) {
        return state.fxRateListContainer.weightedAverage("USDRUB", state.begin + (state.end - state.begin) / 2,
            state.end);
    }

    @Benchmark
    public int testCountList(InitialState state) {
        return state.fxRateListContainer.count();
    }

    @Benchmark
    public int testCountMap(InitialState state) {
        return state.fxRateContainer.count();
    }

    @State(Scope.Thread)
    public static class InitialState {
        FxRateContainer fxRateContainer = new FxRateContainerImpl();
        FxRateContainer fxRateListContainer = new FxRateContainerListImpl();

        Long iterations = 100_000L;

        long begin = Instant.now().toEpochMilli();
        long end = -1;

        @Setup(Level.Trial)
        public void setUp() {

            for (int i = 0; i < iterations; i++) {
                fxRateContainer.add(calcCcyPair(i), generateFxRate(), Instant.now().toEpochMilli());
                fxRateListContainer.add(calcCcyPair(i), generateFxRate(), Instant.now().toEpochMilli());
            }
            end = Instant.now().toEpochMilli();
            fxRateContainer.add("USDRUB", generateFxRate(), end);
            fxRateListContainer.add("USDRUB", generateFxRate(), end);
        }

    }

}
