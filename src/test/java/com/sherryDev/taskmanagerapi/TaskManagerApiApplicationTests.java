package com.sherryDev.taskmanagerapi;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.sherryDev.taskmanagerapi.model.Task;
import com.sherryDev.taskmanagerapi.service.taskService;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import java.net.URI;
import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskManagerApiApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	taskService service;


	@Test
	void contextLoads() {
	}

	@Test
	void shouldReturnRequestedTaskById() {
		//Provide relevant Id
//		int id = 9;
		//Send Get request to api endpoint with specified task and url
		//Store retrieved task in a variable
		ResponseEntity<Task> getReqResponse = restTemplate.getForEntity("/tasks-api/find/9", Task.class);
		Task reqTask = (Task) getReqResponse.getBody();

		//Make assertions based on Response status
		assertThat(getReqResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		//Make assertions based on task Id
		assertThat(reqTask.getTId()).isEqualTo(9);
	}

	@Test
	void shouldNotReturnTaskWithUnknownId() {

		//Use rest template to send get request with invalid Id to API endpoint
		ResponseEntity<String> getReqResponse = restTemplate.getForEntity("/tasks-api/find/3000", String.class);


		//Make assertions based on http status of the response given by get request
		assertThat(getReqResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		//Make assertions based on response body of get response
		//Has to be response entity of type String in order to use isBlank() method
		assertThat(getReqResponse.getBody()).isBlank();
	}

	@Test
	void shouldCreateAndReturnCreatedTask() {

		//Create a new task
		Task saveTask = new Task();
		saveTask.setTName("Tidy Lounge");
		saveTask.setTDescription("Sweep the lounge, tidy and organize couches");

		//Create a post request to api endpoint for creating and saving Task
		ResponseEntity<Void> postReqResponse = restTemplate.postForEntity("/tasks-api/create", saveTask, Void.class);

		//Retrieve URI address of created resource/Task
		URI taskAddr = postReqResponse.getHeaders().getLocation();

		//Create a get request with newly created task URI to API get endpoint
		ResponseEntity<Task> getReqResponse = restTemplate.getForEntity(taskAddr, Task.class);

		//Store retrieved task in variable for later assertions
		Task savedTask = getReqResponse.getBody();

		//Make assertions based on status code of post response
		assertThat(postReqResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		//Make assertions based on get response status code
		assertThat(getReqResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		//Make assertions based on data in retrieved resource and resource created
		//Both hibernate/spring data jpa has different ids generated for same Task thinks it is because of jpa generation strategy set to GenerationType.IDENTITY
//		assertThat(savedTask.getTId()).isEqualTo(saveTask.getTId());

		assertThat(savedTask.getTName()).isEqualTo(saveTask.getTName());

		assertThat(savedTask.getTDescription()).isEqualTo(saveTask.getTDescription());

	}

	@Test
	void shouldReturnAllTasks() {

		//Send get request to API return all endpoint
		ResponseEntity<String> getReqResponse = restTemplate.getForEntity("/tasks-api/findAll", String.class);

		DocumentContext context = JsonPath.parse(getReqResponse.getBody());
		int numTasksRet = context.read("$.length()");
		JSONArray taskIds = context.read("$..tid");


		//Make assertions based on ids received and lengths of count method and returned list
		assertThat(getReqResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		assertThat(numTasksRet).isEqualTo(service.getTotalSavedEntities());

		assertThat(taskIds).containsExactlyInAnyOrder(8,9,10,13,14);

	}


//	@Test
//	void shouldUpdateTaskAndReturnUpdatedTask() {
//		//Get valid saved task
//		ResponseEntity<Task> getReqRes = restTemplate.getForEntity("/tasks-api/find/16",Task.class);
//		Task rTask = getReqRes.getBody();
//
//		//Update retrieved task with getters and setters
//		rTask.setTName("House Maintenance Chores");
//
//		//Save updated task
//		ResponseEntity<Void> postReqRes = restTemplate.postForEntity("/tasks-api/create", rTask, Void.class);
//		URI updatedTaskLocation = postReqRes.getHeaders().getLocation();
//
//		ResponseEntity<Task> getReqResUpdTask = restTemplate.getForEntity(updatedTaskLocation,Task.class);
//		Task updTask = getReqResUpdTask.getBody();
//
//		//Make assertions based on status codes returned from various requests involved in the update procedure
//		assertThat(getReqRes.getStatusCode()).isEqualTo(HttpStatus.OK);
//		assertThat(postReqRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//		assertThat(getReqResUpdTask.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//		//Make assertions based on updated details of task
//		assertThat(updTask.getTName()).isEqualTo("House Maintenance Chores");
//
//
//	}

	@Test
	@DirtiesContext
	void shouldUpdateTask() {
		//Get valid saved task
		ResponseEntity<Task> getReqRes = restTemplate.getForEntity("/tasks-api/find/16",Task.class);
		Task rTask = getReqRes.getBody();

		//Update retrieved task with getters and setters
		rTask.setTName("House Maintenance Tasks");

		ResponseEntity<Task> putReqRes = restTemplate.exchange("/tasks-api/amend/16" ,HttpMethod.PUT ,new HttpEntity<>(rTask) , Task.class);

		Task updTask = putReqRes.getBody();

		assertThat(putReqRes.getBody()).isNull();
		assertThat(getReqRes.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(putReqRes.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}


	@Test
	void shouldNotUpdateTaskWithUnknownId() {

//		//Create task
		Task nTask = new Task();
		nTask.setTName("Maintenance");
		nTask.setTDescription("Clean and tidy both the kitchen area and your room");

		//Update Task with Id does not exist
		ResponseEntity<Task> putReqRes = restTemplate.exchange("/tasks-api/amend/5000", HttpMethod.PUT, new HttpEntity<>(nTask), Task.class);

		//Make assertions based on status code of put operation
		assertThat(putReqRes.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		//Make assertions based on body of put request
		assertThat(putReqRes.getBody()).isNull();
	}


	@Test
	void shouldDeleteTaskById() {

		ResponseEntity<Task> deleteReqRes = restTemplate.exchange("/tasks-api/remove?id=14", HttpMethod.DELETE, new HttpEntity<>(Task.class), Task.class);

		assertThat(deleteReqRes.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		assertThat(deleteReqRes.getBody()).isNull();
	}


	//Add test for remove all tasks
	//Add test for not deleting invalid Id
}
