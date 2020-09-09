package com.camunda.training;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;

@Component
public class TweetDelegate implements JavaDelegate {

    private final Logger LOGGER = LoggerFactory.getLogger(TweetDelegate.class.getName());
    private Twitter twitter = new TwitterFactory().getInstance();
    private String message;
    private TwitterService twitterService;

    @Inject
    public TweetDelegate(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        message = (String) delegateExecution.getVariable("Tweet");
        LOGGER.info("Publishing Tweet: "+ message);
        boolean success = twitterService.sendTweet(message);
        if(success){
            LOGGER.info("Tweet published successfully");
        }else{
            LOGGER.info("FAILED to publish Tweet!");
        }
    }
}
