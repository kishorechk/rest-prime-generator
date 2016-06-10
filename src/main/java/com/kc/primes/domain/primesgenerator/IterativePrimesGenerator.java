package com.kc.primes.domain.primesgenerator;

import java.util.ArrayList;
import java.util.List;

class IterativePrimesGenerator implements PrimesGenerator {

    private final Long upperLimit;
    private final Long lowerLimit;

    @Override
    public Long getUpperLimit() {
        return this.upperLimit;
    }

    IterativePrimesGenerator(Long upperLimit) {
        this(2L, upperLimit);
    }

    private IterativePrimesGenerator(Long lowerLimit, Long upperLimit) {
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }

    @Override
    public List<Long> generatePrimes() {
        List<Long> primes = new ArrayList<>();
        for (long current = lowerLimit; current <= upperLimit; current++) {
            long sqrRoot = (long) Math.sqrt(current);
            boolean isPrime = true;
            for (long i = 2L; i <= sqrRoot; i++) {
                if (current % i == 0) {
                    isPrime = false;
                }
            }
            if (isPrime) {
                primes.add(current);
            }
        }
        return primes;
    }
}
