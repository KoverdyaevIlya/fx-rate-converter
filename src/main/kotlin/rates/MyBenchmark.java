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
    public static class MyState {
        ArrayList<FxRate> fxRatesList = new ArrayList<>();
        FxRateContainer fxRateContainer = new FxRateContainerImpl();


        Long iterations  = 100_000L;
        FxRate fxRate = new FxRate(90.0, 1L);
        int employeeIndex = -1;

        @Setup(Level.Trial)
        public void setUp() {

            for (int i = 0; i < iterations; i++) {
                fxRateContainer.add( isXorEven(i) ? "USDRUB" : "EURRUB", generateFxRate(), Instant.now().toEpochMilli());

                fxRatesList.add(new FxRate(92.0, 2L));
            }

            fxRatesList.add(fxRate);
            employeeIndex = fxRatesList.indexOf(fxRate);

        }

    }

    @Benchmark
    public void testAdd( MyState state) {

        state.fxRatesList.add(new FxRate(state.iterations.doubleValue(), Instant.now().toEpochMilli()));
    }

    @Benchmark
    public void testAddMap( MyState state) {

        state.fxRateContainer.add("USDRUB", generateFxRate(), Instant.now().toEpochMilli());
    }


    @Benchmark
    public void testAddAt( MyState state) {
        state.fxRatesList.add(state.iterations.intValue(), new FxRate(state.iterations.doubleValue(), Instant.now().toEpochMilli()));
    }

    @Benchmark
    public Boolean testContains(MyState state)  {
        return state.fxRatesList.contains(state.fxRate);
    }

    @Benchmark
    public int testIndexOf(MyState state)  {
        return state.fxRatesList.indexOf(state.fxRate);
    }

    @Benchmark
    public FxRate testGet(MyState state) {
        return state.fxRatesList.get(state.employeeIndex);
    }

    @Benchmark
    public Boolean testRemove(MyState state)  {
        return state.fxRatesList.remove(state.fxRate);
    }

    private static double generateFxRate() {
        double leftLimit = 80D;
        double rightLimit = 95D;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private static boolean isXorEven(int x) {
        return (x ^ 1) > x;
    }

}
