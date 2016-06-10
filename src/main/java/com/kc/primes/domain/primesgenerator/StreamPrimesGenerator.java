package com.skua.primes.domain.primesgenerator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

class StreamPrimesGenerator implements PrimesGenerator {

    private final Long upperLimit;
    private final Long lowerLimit;

    @Override
    public Long getUpperLimit() {
        return this.upperLimit;
    }

    StreamPrimesGenerator(Long upperLimit) {
        this(2L, upperLimit);
    }

    private StreamPrimesGenerator(Long lowerLimit, Long upperLimit) {
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }

    @Override
    public List<Long> generatePrimes() {
        return LongStream
                .rangeClosed(lowerLimit, upperLimit)
                .filter(i -> isPrime(i))
                .boxed()
                .collect(Collectors.toList());
    }

    private boolean isPrime(long x) {
        return LongStream
                .rangeClosed(2L, (long) (Math.sqrt(x)))
                .allMatch(n -> x % n != 0);
    }
}
