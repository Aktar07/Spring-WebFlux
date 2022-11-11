package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux(){
        return Flux.fromIterable(List.of("alex","ben","chloe")).log(); //db or remote service call
    }

    public Flux<String> namesFlux_map(int stringLength){
        //filter the string whose length is greater than 3

        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .map(s -> s.length()+"-"+s)
                //.map(s -> s.toUpperCase())
                .log();
    }

    public Flux<String> namesFlux_immutability(){
        var namesFlux = Flux.fromIterable(List.of("alex","ben","chloe"));
        namesFlux.map(String::toUpperCase);
        return namesFlux;

    }

    public Mono<String> nameMono(){
        return Mono.just("alex").log();
    }

    public Flux<String> namesFlux_flatMap(int stringLength) {
        //filter the string whose length is greater than 3

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(s -> splitString(s))
                .log();
    }

    public Flux<String> splitString(String name){
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> namesFlux_flatMap_async(int stringLength){
        //filter the string whose length is greater than 3

        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(s -> splitString_withDelay(s))
                .log();
    }

    public Mono<List<String>> namesMono_flatMap(int stringLength){
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    private Mono<List<String>> splitStringMono(String s){
        var charArray = s.split("");
        var charList = List.of(charArray);
        return Mono.just(charList);
    }

    public Flux<String> namesFlux_concatMap(int stringLength){
        //filter the string whose length is greater than 3

        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .concatMap(s -> splitString_withDelay(s))
                .log();
    }

    public Flux<String> namesFlux_transform(int stringLength) {
        //filter the string whose length is greater than 3

        Function<Flux<String>,Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .flatMap(s -> splitString(s))
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFlux_transform_switchIfEmpty(int stringLength) {
        //filter the string whose length is greater than 3

        Function<Flux<String>,Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(s -> splitString(s));

        var defaultFlux = Flux.just("default")
                .transform(filterMap);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public Flux<String> explore_concat(){
        var abcFlux = Flux.just("A","B","C");
        var defFlux =Flux.just("D","E","F");

        return Flux.concat(abcFlux,defFlux);
    }

    public Flux<String> explore_concatwith(){
        var abcFlux = Flux.just("A","B","C");
        var defFlux =Flux.just("D","E","F");

        return abcFlux.concatWith(defFlux).log();
    }



    public Flux<String> explore_concatwith_mono(){
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");

        return aMono.concatWith(bMono).log();
    }

    public Flux<String> explore_merge(){
        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));
        var defFlux =Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));

        return Flux.merge(abcFlux,defFlux).log();
    }

    public Flux<String> explore_mergewith(){
        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));
        var defFlux =Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));

        return abcFlux.mergeWith(defFlux).log();
    }

    public Flux<String> explore_mergewith_mono(){
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");

        return aMono.mergeWith(bMono).log();
    }

    public Flux<String> explore_mergeSequential(){
        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));
        var defFlux =Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));

        return Flux.mergeSequential(abcFlux,defFlux).log();
    }

    public Flux<String> explore_zip(){
        var abcFlux = Flux.just("A","B","C");
        var defFlux =Flux.just("D","E","F");

        return Flux.zip(abcFlux,defFlux,(f,s)-> f+s).log();
    }

    public Flux<String> explore_zip_1(){
        var abcFlux = Flux.just("A","B","C");
        var defFlux =Flux.just("D","E","F");
        var _123flux = Flux.just("1","2","3");
        var _456flux = Flux.just("4","5","6");
        return Flux.zip(abcFlux,defFlux,_123flux,_456flux)
                .map(t4 -> t4.getT1()+t4.getT2()+t4.getT3()+t4.getT4()).log();

    }

    public Flux<String> explore_zipWith(){
        var abcFlux = Flux.just("A","B","C");
        var defFlux =Flux.just("D","E","F");

        return abcFlux.zipWith(defFlux,(f,s)-> f+s).log();
    }

    public Mono<String> explore_zipwith_mono(){
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");

        return aMono.zipWith(bMono)
                .map(t2 -> t2.getT1()+t2.getT2()).log();
    }



    public Flux<String> splitString_withDelay(String name){
        var charArray = name.split("");
        // var delay = new Random().nextInt(1000);
        var delay = 1000;
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
    }

    public Flux<String> namesMono_flatMapMany(int stringLength){
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMapMany(this::splitString)
                .log();
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> {
                    System.out.println("Name is : "+name);
                });

        fluxAndMonoGeneratorService.nameMono()
                .subscribe(name -> {
                    System.out.println("Mono name is : "+name);
                });

    }
}

