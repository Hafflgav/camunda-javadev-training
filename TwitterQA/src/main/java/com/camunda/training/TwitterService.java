package com.camunda.training;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

@Service
public class TwitterService {

    private Twitter twitter = new TwitterFactory().getInstance();
    private final AccessToken ACCESS_TOKEN = new AccessToken("220324559-jet1dkzhSOeDWdaclI48z5txJRFLCnLOK45qStvo", "B28Ze8VDucBdiE38aVQqTxOyPc7eHunxBVv7XgGim4say");
    private final Logger LOGGER = LoggerFactory.getLogger(TwitterService.class.getName());

    public TwitterService(){
        twitter.setOAuthConsumer("lRhS80iIXXQtm6LM03awjvrvk", "gabtxwW8lnSL9yQUNdzAfgBOgIMSRqh7MegQs79GlKVWF36qLS");
        twitter.setOAuthAccessToken(ACCESS_TOKEN);
    }

    public boolean sendTweet(String message)  {
        boolean success=false;
        try {
            twitter.updateStatus(message);
            LOGGER.info("Tweet published successfully");
            success= true;
        } catch (TwitterException exception){
            LOGGER.info("FAILED to publish Tweet!");
            throw new BpmnError("duplicateTweet");
        }
        return success;
    }
}
