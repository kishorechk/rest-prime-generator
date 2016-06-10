package com.kc.primes.main;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.kc.primes.PrimeGeneratorServiceConfiguration;
import org.springframework.boot.SpringApplication;

import java.util.concurrent.TimeUnit;

public class PrimeGeneratorServiceMain {
    
    public static void main(String[] args) {
        SpringApplication.run(PrimeGeneratorServiceConfiguration.class, args);
    }
}
