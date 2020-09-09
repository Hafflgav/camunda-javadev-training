package com.camunda.training;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProcessJUnitTest {

  @Rule
  public ProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder
          .create()
          .build();

  @Before
  public void setup() {
    init(rule.getProcessEngine());
    Mocks.register("tweetDelegate", new LoggerDelegate());
  }


  @Test
  @Deployment(resources = "training.bpmn")
  public void testTweetDelegate(){
    //given
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("Tweet", "Make the world a better place!"+ UUID.randomUUID());
    variables.put("approval", true);

    //When
    ProcessInstance processInstance = runtimeService()
            .startProcessInstanceByKey("Process_TwitterQA", variables);
    Task task = taskService()
            .createTaskQuery()
            .singleResult();
    taskService().complete(task.getId());
    List<Job> jobList = jobQuery()
            .processInstanceId(processInstance.getId())
            .list();
    Job job = jobList.get(0);
    execute(job);

    // Then
    assertThat(processInstance).isEnded();
  }

  @Test
  @Deployment(resources = "training.bpmn")
  public void testTaskAssertion(){
    // Given
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("Tweet", "Make Camunda Great Again!");
    variables.put("approval", false);

    // When
    ProcessInstance processInstance = runtimeService()
            .startProcessInstanceByKey("Process_TwitterQA", variables);
    List<Task> taskList = taskService()
            .createTaskQuery()
            .taskCandidateGroup("management")
            .list();

    // Then
    assertNotNull(taskList);
    assertEquals(1, taskList.size() );
  }

  @Test
  @Deployment(resources="training.bpmn")
  public void testSadPath() {
    // Given
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("Tweet", "Make Camunda Great Again!");
    variables.put("approval", false);

    // When
    ProcessInstance processInstance = runtimeService()
            .startProcessInstanceByKey("Process_TwitterQA", variables);
    Task task = taskService()
            .createTaskQuery()
            .singleResult();
    assertEquals("Approve tweet", task.getName());
    taskService().complete(task.getId());

    // Then
    assertEquals(0, runtimeService().createProcessInstanceQuery().count());
    assertThat(processInstance).isEnded();
  }
}