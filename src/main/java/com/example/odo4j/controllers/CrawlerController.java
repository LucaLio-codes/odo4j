package com.example.odo4j.controllers;

import com.example.odo4j.assemblers.TwitterCrawlerAssembler;
import com.example.odo4j.models.TwitterCrawlerModel;
import com.example.odo4j.repos.TweetRepository;
import com.example.odo4j.repos.TwitterCrawlerRepository;
import exceptions.JobNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import util.CrawlerRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Transactional
@CrossOrigin
public class CrawlerController {

    private final TwitterCrawlerRepository repository;
    private final TwitterCrawlerAssembler assembler;
    private final CrawlerRunner crawlerRunner;
    private final TweetRepository tweetRepository;

    CrawlerController(TwitterCrawlerRepository repository, TwitterCrawlerAssembler assembler, TweetRepository tweetRepository){
        this.repository = repository;
        this.assembler = assembler;
        this.tweetRepository = tweetRepository;
        crawlerRunner = new CrawlerRunner();
    }

    @GetMapping(value = "/twitter", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<EntityModel<TwitterCrawlerModel>> all(){
        List<EntityModel<TwitterCrawlerModel>> jobs = repository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(jobs, linkTo(methodOn(CrawlerController.class).all()).withSelfRel());
    }

    @PostMapping(value = "/twitter", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<TwitterCrawlerModel> add(@RequestBody TwitterCrawlerModel newCrawler){
        return assembler.toModel(repository.save(newCrawler));
    }

    @GetMapping(value = "twitter/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<TwitterCrawlerModel> one(@PathVariable long id){
        TwitterCrawlerModel crawler = repository.findById(id).orElseThrow(() -> new JobNotFoundException(id));
        return assembler.toModel(crawler);
    }

    @PutMapping(value = "twitter/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<TwitterCrawlerModel> replaceJob(@PathVariable long id, @RequestBody TwitterCrawlerModel newCrawler){
        TwitterCrawlerModel crawler = repository.findById(id).map(job -> {
            job.setSearchQuery(newCrawler.getSearchQuery());
            return repository.save(job);
        }).orElseGet(() -> {
            newCrawler.setId(id);
            return repository.save(newCrawler);
        });
        return assembler.toModel(crawler);
    }

    @DeleteMapping(value = "twitter/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteJob(@PathVariable long id){
        repository.deleteById(id);
        return ResponseEntity.accepted().body(id);
    }

    @GetMapping(value = "start/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<TwitterCrawlerModel> start(@PathVariable long id){
        TwitterCrawlerModel crawler = one(id).getContent();

        assert crawler != null;
        crawlerRunner.startCrawler(crawler, tweetRepository);
        return assembler.toModel(crawler);
    }

    @GetMapping(value = "/stop/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<TwitterCrawlerModel> stop(@PathVariable long id){
        TwitterCrawlerModel crawler = one(id).getContent();
        assert crawler != null;
        crawlerRunner.interruptCrawler(crawler);
        return assembler.toModel(crawler);
    }




}
