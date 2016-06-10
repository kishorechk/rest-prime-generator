package com.kc.primes.api.controller;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Timed;
import com.kc.primes.domain.PrimesResult;
import com.kc.primes.domain.primesgenerator.PrimesGenerator.PrimesStrategy;
import com.kc.primes.service.PrimesService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/primes")
public class PrimesController {

    @Autowired
    private PrimesService primesService;

    @RequestMapping(value = "/strategy/default/{upperLimit:\\d+}", method = RequestMethod.GET)
    @Timed
        public ResponseEntity<PrimesResult> generatePrimesWithDefaultStrategy(@ApiParam(value = "the upper limit of primes to be generated")
                                                                          @PathVariable("upperLimit") Long upperLimit) {
        return new ResponseEntity<>(this.primesService.generatePrime(upperLimit, Optional.empty()), HttpStatus.OK);
    }

    @RequestMapping(value = "/strategy/{strategyName}/{upperLimit:\\d+}", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<PrimesResult> generatePrimes(
            @ApiParam(value = "the strategy to use.")
            @PathVariable("strategyName") PrimesStrategy strategyName,

            @ApiParam(value = "the upper limit of primes to be generated")
            @PathVariable("upperLimit") Long upperLimit) {
        return new ResponseEntity<>(this.primesService.generatePrime(upperLimit, Optional.of(strategyName)), HttpStatus.OK);
    }

    @RequestMapping(value = "/{uuid}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrimesResult> getGeneratedPrimesById(@PathVariable("uuid") String resultId) {
        Optional<PrimesResult> primesResultOptional = this.primesService.getPrimesResult(resultId);
        PrimesResult primesResult;
        if (!primesResultOptional.isPresent()) {
            primesResult = new PrimesResult(null);
            primesResult.setErrorMessage("Result Not Found");
        } else {
            primesResult = primesResultOptional.get();
        }
        return new ResponseEntity<>(primesResult, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PrimesResult>> getAllGeneratedPrimes() {
        return new ResponseEntity<>(this.primesService.getPrimesResults(), HttpStatus.OK);
    }

    @RequestMapping(value = "/strategyNames",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<String>> getPrimesStrategyNames() {
        return new ResponseEntity<>(
                Stream.of(PrimesStrategy.values())
                        .map(PrimesStrategy::name)
                        .collect(Collectors.toList())
                , HttpStatus.OK);
    }
}
