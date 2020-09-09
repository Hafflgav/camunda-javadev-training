package com.camunda.training;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class LoggerDelegate implements JavaDelegate {

    private final Logger LOGGER = LoggerFactory.getLogger(TweetDelegate.class.getName());
    private String message;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        message = (String) delegateExecution.getVariable("Tweet");
        LOGGER.info("Publishing Tweet: "+ message);
        LOGGER.info("Tweet published successfully");
    }
}
