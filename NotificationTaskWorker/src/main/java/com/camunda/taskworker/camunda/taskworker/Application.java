package com.camunda.taskworker.camunda.taskworker;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.topic.TopicSubscriptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLOutput;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@SpringBootApplication
public class Application {

	private static final String TOPIC = "notification";
	private static final String CAMUNDAENDPOINT = "http://localhost:8080/engine-rest";
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		ExternalTaskClient client = ExternalTaskClient.create()
				.baseUrl(CAMUNDAENDPOINT)
				.asyncResponseTimeout(10000)
				.disableBackoffStrategy()
				.maxTasks(1)
				.build();

		TopicSubscriptionBuilder subscriptionBuilder = client
				.subscribe(TOPIC)
				.lockDuration(20000)
				.handler((externalTask, externalTaskService) -> {

					String notification = createNotificationFromVariables(externalTask);
					try {
						LOGGER.info("Your Tweet has been rejected due to the following reasons: "+ notification);
						externalTaskService.complete(externalTask);
					} catch (Exception e) {
						LOGGER.info((Marker) Level.SEVERE, "Exception occured", e);
					}
				});

		client.start();
		subscriptionBuilder.open();
	}

	private static String createNotificationFromVariables(ExternalTask externalTask){
		String notification = new String();
		try{
			notification = externalTask.getVariable("Tweet");
		}catch (Exception exception){
			LOGGER.info((Marker) Level.SEVERE, "Exception occured", exception);
		}
		return notification;
	}
}
