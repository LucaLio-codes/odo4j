package com.example.odo4j.assemblers;

import com.example.odo4j.controllers.CrawlerController;
import com.example.odo4j.controllers.TweetController;
import com.example.odo4j.models.TwitterCrawlerModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TwitterCrawlerAssembler implements RepresentationModelAssembler<TwitterCrawlerModel, EntityModel<TwitterCrawlerModel>> {


    @Override
    public EntityModel<TwitterCrawlerModel> toModel(TwitterCrawlerModel entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(CrawlerController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(CrawlerController.class).deleteJob(entity.getId())).withRel("delete"),
                linkTo(methodOn(CrawlerController.class).start(entity.getId())).withRel("start"),
                linkTo(methodOn(CrawlerController.class).stop(entity.getId())).withRel("stop"),
                linkTo(methodOn(TweetController.class).byCrawler(entity.getId())).withRel("tweets"),
                linkTo(methodOn(CrawlerController.class).all()).withRel("jobs")
                );
    }
}
