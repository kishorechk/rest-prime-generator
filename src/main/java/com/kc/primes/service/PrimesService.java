package com.kc.primes.service;

import com.kc.primes.domain.PrimesResult;
import com.kc.primes.domain.primesgenerator.PrimesGenerator;

import java.util.List;
import java.util.Optional;

public interface PrimesService {
    List<PrimesResult> getPrimesResults();

    Optional<PrimesResult> getPrimesResult(String resultId);

    PrimesResult generatePrime(Long upperLimit, Optional<PrimesGenerator.PrimesStrategy> algorithm);

    void pruneCacheOlderThanInterval(long intervalInMinutes);
}
