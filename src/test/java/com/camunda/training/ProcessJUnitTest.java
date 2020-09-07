package com.camunda.training;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;

public class ProcessJUnitTest {

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule();

  @Before
  public void setup() {
    init(rule.getProcessEngine());
  }

  @Test
  @Deployment(resources="training.bpmn")
  public void testHappyPath() {
    // Given
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("Tweet", "Make Camunda Great Again!");
    variables.put("approval", true);

    // When
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("Process_TwitterQA", variables);
    Task task = taskService().createTaskQuery().singleResult();
    taskService().complete(task.getId());

    // Then
    assertThat(processInstance).isEnded();
  }
}