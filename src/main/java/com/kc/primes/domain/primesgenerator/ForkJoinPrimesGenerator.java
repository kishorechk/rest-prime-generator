package com.kc.primes.domain.primesgenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

class ForkJoinPrimesGenerator extends RecursiveTask<List<Long>> implements PrimesGenerator {
    private static final Logger log = LoggerFactory.getLogger(ForkJoinPrimesGenerator.class);

    public static final long THRESHOLD = 4000;

    private final Long upperLimit;
    private final Long lowerLimit;

    @Override
    public List<Long> generatePrimes() {
        return compute();
    }

    @Override
    public Long getUpperLimit() {
        return this.upperLimit;
    }

    ForkJoinPrimesGenerator(Long upperLimit) {
        this(2L, upperLimit);
    }

    private ForkJoinPrimesGenerator(Long lowerLimit, Long upperLimit) {
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }

    @Override
    protected List<Long> compute() {
        Long length = upperLimit - lowerLimit;
        if (length <= THRESHOLD) {
            return computeSequentially();
        }
        ForkJoinPrimesGenerator leftTask = new ForkJoinPrimesGenerator(lowerLimit, lowerLimit + length / 2 - 1L);
        leftTask.fork();
        ForkJoinPrimesGenerator rightTask = new ForkJoinPrimesGenerator(lowerLimit + length / 2, upperLimit);
        List<Long> rightResult = rightTask.compute();
        List<Long> leftResult = leftTask.join();

        log.debug("Lower {}, Upper {}", lowerLimit, upperLimit);
        log.debug("Left {}", leftResult);
        log.debug("Right {}", rightResult);

        rightResult.addAll(leftResult);
        return rightResult;
    }

    private List<Long> computeSequentially() {
        log.debug("Computing Sequentially Lower {}, Upper {}", lowerLimit, upperLimit);
        List<Long> primes = LongStream
                .rangeClosed(lowerLimit, upperLimit)
                .filter(i ->
                        LongStream
                                .rangeClosed(2L, (long) (Math.sqrt(i)))
                                .allMatch(n -> i % n != 0)
                )
                .boxed()
                .collect(Collectors.toList());
        log.debug("Computed {}", primes);
        return primes;
    }
}
