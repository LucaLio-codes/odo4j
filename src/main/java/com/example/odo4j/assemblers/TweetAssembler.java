package com.example.odo4j.assemblers;

import com.example.odo4j.controllers.CrawlerController;
import com.example.odo4j.controllers.TweetController;
import com.example.odo4j.models.Tweet;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TweetAssembler implements RepresentationModelAssembler<Tweet, EntityModel<Tweet>> {

    @Override
    public EntityModel<Tweet> toModel(Tweet entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(TweetController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(TweetController.class).all()).withRel("tweets"),
                linkTo(methodOn(TweetController.class).byCrawler(entity.getCrawler().getId())).withRel("from crawler"));

    }
}
