package com.kc.primes.domain.primesgenerator;

import com.kc.primes.domain.PrimesResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class PrimesGeneratorHarness {
    private static final Logger log = LoggerFactory.getLogger(PrimesGeneratorHarness.class);

    BlockingQueue<PrimesResult> sharedQueue = new LinkedBlockingQueue<>();

    public void queueForProcessing(PrimesResult primesResult) {
        try {
            this.sharedQueue.put(primesResult);
            log.info("Queued {}", primesResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
            primesResult.setErrorMessage(e.getMessage());
        }
    }

    @PostConstruct
    public void initIt() {
        new Thread(new Consumer(this.sharedQueue))
                .start();
        log.info("Initialized PrimesGeneratorHarness");
    }

    protected List<Long> generatePrimes(Long upperLimit, PrimesGenerator.PrimesStrategy algorithm) {
        PrimesGenerator primesGenerator = null;
        switch (algorithm) {
            case FORK_JOIN:
                primesGenerator = new ForkJoinPrimesGenerator(upperLimit);
                break;
            case ITERATIVE:
                primesGenerator = new IterativePrimesGenerator(upperLimit);
                break;
            case STREAM:
                primesGenerator = new StreamPrimesGenerator(upperLimit);
                break;
            case PARALLEL_STREAM:
                primesGenerator = new ParallelStreamPrimesGenerator(upperLimit);
                break;
            case ERATOSTHENES_SIEVE:
                if (upperLimit > Integer.MAX_VALUE - 1) {
                    throw new IllegalArgumentException("Upper Limit has to lower than Integer Max Value for Sieve Algorithm");
                }
                primesGenerator = new SieveOfEratosthenesPrimesGenerator(upperLimit.intValue());
                break;

        }
        log.info("Triggering Prime Generation For {} Using {}", upperLimit, algorithm);
        return primesGenerator.generatePrimes();
    }

    private class Consumer implements Runnable {

        private final BlockingQueue<PrimesResult> sharedQueue;

        public Consumer(BlockingQueue<PrimesResult> sharedQueue) {
            this.sharedQueue = sharedQueue;
        }

        @Override
        public void run() {
            while (true) {
                PrimesResult primesResult = null;
                try {
                    primesResult = sharedQueue.take();
                    log.info("Processing {}", primesResult);
                    Long startTime = System.nanoTime();
                    List<Long> primes = generatePrimes(primesResult.getUpperLimit(), primesResult.getPrimesStrategy());
                    Long endTime = System.nanoTime();
                    primesResult.addAll(primes);
                    primesResult.setProcessingTimeInNanoSeconds(endTime - startTime);
                    log.info("Processed {}", primesResult);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    primesResult.setErrorMessage(e.getMessage());
                }
            }
        }
    }
}
