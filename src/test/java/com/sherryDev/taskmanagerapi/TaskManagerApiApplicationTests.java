package com.sherryDev.taskmanagerapi;

import com.sherryDev.taskmanagerapi.model.Task;
import com.sherryDev.taskmanagerapi.service.taskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskManagerApiApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;


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
}
