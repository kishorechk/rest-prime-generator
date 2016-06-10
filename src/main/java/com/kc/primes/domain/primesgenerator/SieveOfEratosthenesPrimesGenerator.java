package com.skua.primes.domain.primesgenerator;


import java.util.ArrayList;
import java.util.List;

public class SieveOfEratosthenesPrimesGenerator implements PrimesGenerator {

    private final Integer upperLimit;
    private final Integer lowerLimit;

    @Override
    public Long getUpperLimit() {
        return this.upperLimit.longValue();
    }

    SieveOfEratosthenesPrimesGenerator(Integer upperLimit) {
        this(2, upperLimit);
    }

    private SieveOfEratosthenesPrimesGenerator(Integer lowerLimit, Integer upperLimit) {
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }

    @Override
    public List<Long> generatePrimes() {
        List<Long> primes = new ArrayList<>();
        boolean[] isComposite = new boolean[upperLimit + 1]; // limit + 1 because we won't use '0'th index of the array
        isComposite[1] = true;

        // Mark all composite numbers
        for (int i = 2; i <= upperLimit; i++) {
            if (!isComposite[i]) {
                primes.add(Long.valueOf(i));
                int multiple = 2;
                while (i * multiple <= upperLimit) {
                    isComposite[i * multiple] = true;
                    multiple++;
                }
            }
        }
        return primes;
    }
}
