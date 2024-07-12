package rates;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 10)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class MyBenchmark {

    @Benchmark
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.Throughput)
    public void init(Blackhole bh) {
    }

    @State(Scope.Thread)
    public static class InitialState {
        ArrayList<FxRate> fxRatesList = new ArrayList<>();
        FxRateContainer fxRateContainer = new FxRateContainerImpl();


        Long iterations  = 100_000L;
        FxRate fxRate = new FxRate(90.0, 1L);

        long begin = Instant.now().toEpochMilli();
        long end = -1;

        @Setup(Level.Trial)
        public void setUp() {

            for (int i = 0; i < iterations; i++) {
                fxRateContainer.add( calcCcyPair(i), generateFxRate(), Instant.now().toEpochMilli());

                fxRatesList.add(new FxRate(92.0, 2L));
            }

            end = Instant.now().toEpochMilli();
            fxRateContainer.add( "USDRUB", generateFxRate(), end);



        }

    }

    @Benchmark
    public void testAdd( InitialState state) {
        state.fxRatesList.add(new FxRate(state.iterations.doubleValue(), Instant.now().toEpochMilli()));
    }

    @Benchmark
    public void testAddMap( InitialState state) {

        state.fxRateContainer.add("USDRUB", generateFxRate(), Instant.now().toEpochMilli());
    }

    @Benchmark
    public Double testGetMap(InitialState state) {
        return state.fxRateContainer.get("USDRUB", state.end);
    }

    @Benchmark
    public Double testAverageMap(InitialState state) {
        return state.fxRateContainer.average("EURRUB", state.begin + (state.end - state.begin)/2, state.end);
    }

    @Benchmark
    public Double testWeightedAverageMap(InitialState state) {
        return state.fxRateContainer.weightedAverage("USDRUB", state.begin + (state.end - state.begin)/2, state.end);
    }


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

}
