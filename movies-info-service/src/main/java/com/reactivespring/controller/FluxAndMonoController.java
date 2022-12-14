package com.reactivespring.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxAndMonoController {

    //non-blocking API - flux
    @GetMapping("/flux")
    public Flux<Integer> flux(){
        return Flux.just(1,2,3).log();
    }

    //non-blocking API - mono
    @GetMapping("/mono")
    public Mono<String> helloWorldMono(){
        return Mono.just("hello-world").log();
    }

    //Infinite streams API(SSE)
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> stream(){
        return Flux.interval(Duration.ofSeconds(1)).log();
    }
}
