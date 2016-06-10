package com.kc.primes.service;

import com.kc.primes.domain.PrimesResult;
import com.kc.primes.domain.primesgenerator.PrimesGenerator.PrimesStrategy;
import com.kc.primes.domain.primesgenerator.PrimesGeneratorHarness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrimesServiceImpl implements PrimesService {
    private static final Logger log = LoggerFactory.getLogger(PrimesServiceImpl.class);

    Map<String, PrimesResult> resultCache = new HashMap<>();

    @Autowired
    private PrimesGeneratorHarness primesGeneratorHarness;

    @Override
    public List<PrimesResult> getPrimesResults() {
        return this.resultCache.values()
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PrimesResult> getPrimesResult(final String resultId) {
        return this.resultCache.values()
                .stream()
                .filter(r -> r.getResultId().equals(resultId))
                .findAny();
    }

    @Override
    public PrimesResult generatePrime(Long upperLimit, Optional<PrimesStrategy> algorithm) {
        PrimesResult primesResult = validatePrimesInput(upperLimit, algorithm);
        this.primesGeneratorHarness.queueForProcessing(primesResult);
        resultCache.put(primesResult.getResultId(), primesResult);
        return primesResult;
    }

    /*
    * Remove objects which are older than given interval
    * */
    @Override
    public void pruneCacheOlderThanInterval(long intervalInMinutes) {
        List<String> keys = new ArrayList<>();
        resultCache.values()
                .stream()
                .filter(p -> p.getCreateTime().compareTo(LocalDateTime.now().minusMinutes(intervalInMinutes)) < 0)
                .forEach(k -> {
                    log.info("Removing Key {} Created At {}", k.getResultId(), k.getCreateTime());
                    keys.add(k.getResultId());
                });
        keys.stream()
                .forEach(k -> resultCache.remove(k));
    }

    private PrimesResult validatePrimesInput(Long upperLimit, Optional<PrimesStrategy> primesStrategy) {
        if (upperLimit <= 0) {
            throw new IllegalArgumentException("Input Number must be greater than 0");
        }
        if (primesStrategy.isPresent()) {
            return new PrimesResult(upperLimit, primesStrategy.get());
        } else {
            return new PrimesResult(upperLimit);
        }
    }
}
