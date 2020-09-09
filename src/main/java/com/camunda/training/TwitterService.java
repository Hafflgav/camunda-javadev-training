package com.camunda.training;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterService {

    private final AccessToken ACCESS_TOKEN = new AccessToken("220324559-jet1dkzhSOeDWdaclI48z5txJRFLCnLOK45qStvo", "B28Ze8VDucBdiE38aVQqTxOyPc7eHunxBVv7XgGim4say");
    private Twitter twitter = new TwitterFactory().getInstance();

    public boolean sendTweet(String message)  {
        twitter.setOAuthConsumer("lRhS80iIXXQtm6LM03awjvrvk", "gabtxwW8lnSL9yQUNdzAfgBOgIMSRqh7MegQs79GlKVWF36qLS");
        twitter.setOAuthAccessToken(ACCESS_TOKEN);

        try {
            twitter.updateStatus(message);
        } catch (TwitterException exception){
            return false;
        }
        return true;
    }
}
