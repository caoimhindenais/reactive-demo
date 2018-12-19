package com.example.demostream;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RestController
public class CalculateController {



    @GetMapping(value = "/random")
    public Integer getRandom() {
        return IntStream.range(0,10).boxed()
                .map(this::heavyLifting)
                .reduce((a,b)-> a>b?a:b).get();
    }


    @GetMapping(value = "/random-parallel")
    public Integer getRandomParrallel() {
        return IntStream.range(0,10).boxed().parallel()
                .map(this::heavyLifting)
                .reduce((a,b)-> a>b?a:b).get();
    }

    private Integer heavyLifting (Integer i ) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i;
    }


    @GetMapping(value = "/future")
    public Double getFuture() throws ExecutionException, InterruptedException {
        CompletableFuture<Double> f1 =  CompletableFuture.supplyAsync(this::predictFutureInterestRate);
        log.info("Gave the job to the future calculator");
        return  f1.get();

    }

    //Async NIO better use of your threads. Non-Blocking
    @GetMapping(value = "/future-average")
    public Double getFutureAverage() throws ExecutionException, InterruptedException {


        CompletableFuture<Double> f1 =  CompletableFuture.supplyAsync(this::predictFutureInterestRate);
        CompletableFuture<Double> f2 =  CompletableFuture.supplyAsync(this::predictFutureInterestRate);
        log.info("Gave the job to the future calculator");
        CompletableFuture.allOf(f1, f2).join();

        Double aDouble = f1.get();
        Double bDouble = f2.get();
        List<Double> answers = new ArrayList<>();
        answers.add(aDouble);
        answers.add(bDouble);

        OptionalDouble average = answers.stream().mapToDouble(d -> d).average();

        return  average.getAsDouble();

    }

    private Double predictFutureInterestRate() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Finished complex calculation");
        return Math.random()*10;
    }



    //Wait for everything
    @GetMapping("/batch")
    public List<Integer> batchList() {
        return IntStream.range(0,100).boxed().collect(Collectors.toList());
    }


    //Event Stream, back pressure
    @GetMapping(value="/event-stream", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> eventStream() {
        log.info("Handling the request");
        return Flux.range(0,100).delayElements(Duration.ofSeconds(1)).log("Working!");
    }

}

