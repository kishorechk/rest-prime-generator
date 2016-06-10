package com.kc.primes.config;

import com.kc.primes.service.PrimesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private static final Logger log = LoggerFactory.getLogger(SchedulerConfig.class);

    @Autowired
    private PrimesService primesService;

    /*
     * Run every 15 min.
     */
    @Scheduled(cron = "0 0/15 * * * *")
    public void triggerPrimesCachePrune() throws Exception {
        this.primesService.pruneCacheOlderThanInterval(180L);
        log.info("Primes Cache Pruned Older Than 180 Min");
    }
}
