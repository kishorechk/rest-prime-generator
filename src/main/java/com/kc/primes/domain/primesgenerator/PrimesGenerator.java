package com.kc.primes.domain.primesgenerator;

import java.util.List;

public interface PrimesGenerator {
    List<Long> generatePrimes();

    Long getUpperLimit();

    enum PrimesStrategy {
        FORK_JOIN, ITERATIVE, PARALLEL_STREAM, STREAM, ERATOSTHENES_SIEVE;
    }
}
