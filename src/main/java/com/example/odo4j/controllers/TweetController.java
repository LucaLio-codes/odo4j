package com.example.odo4j.controllers;

import com.example.odo4j.assemblers.TweetAssembler;
import com.example.odo4j.assemblers.TwitterCrawlerAssembler;
import com.example.odo4j.models.Tweet;
import com.example.odo4j.models.TwitterCrawlerModel;
import com.example.odo4j.repos.TweetRepository;
import com.example.odo4j.repos.TwitterCrawlerRepository;
import exceptions.JobNotFoundException;
import exceptions.TweetNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin
@RestController
public class TweetController {
    private final TweetRepository repository;
    private final TwitterCrawlerRepository crawlerRepository;
    private final TweetAssembler tweetAssembler;
    private final TwitterCrawlerAssembler crawlerAssembler;

    TweetController(TweetRepository repository, TwitterCrawlerRepository crawlerRepository, TweetAssembler assembler, TwitterCrawlerAssembler crawlerAssembler) {
        this.repository = repository;
        this.crawlerRepository = crawlerRepository;
        this.tweetAssembler = assembler;
        this.crawlerAssembler = crawlerAssembler;
    }

    @GetMapping(value = "/tweets", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<EntityModel<Tweet>> all() {
        List<EntityModel<Tweet>> tweets = repository.findAll().stream().map(tweetAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(tweets, linkTo(methodOn(TweetController.class).all()).withSelfRel());
    }

    @PostMapping(value = "/tweets", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<Tweet> add(@RequestBody Tweet tweet){
        return tweetAssembler.toModel(tweet);
    }

    @GetMapping(value = "/tweets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<Tweet> one(@PathVariable long id) {
        Tweet tweet = repository.findById(id).orElseThrow(() -> new TweetNotFoundException(id));
        return tweetAssembler.toModel(tweet);
    }

    @GetMapping(value = "/tweets-by-crawler/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<EntityModel<Tweet>> byCrawler(@PathVariable long id) {
        TwitterCrawlerModel crawler = crawlerRepository.findById(id).orElseThrow(()-> new JobNotFoundException(id));
        List<EntityModel<Tweet>> tweets = repository.findAllByCrawler(crawler).stream().map(tweetAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(tweets,
                crawlerAssembler.toModel(crawler).getLinks()
                );
    }

}
