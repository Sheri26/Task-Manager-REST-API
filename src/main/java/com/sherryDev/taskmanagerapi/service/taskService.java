package com.sherryDev.taskmanagerapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sherryDev.taskmanagerapi.model.Task;
import com.sherryDev.taskmanagerapi.repository.TaskRepository;

@Service
public class taskService {
	
	@Autowired
	TaskRepository repository;
	
	public Task saveTask(Task task) {
		Task savedTask = repository.save(task);
		return savedTask;
	}
	
	public Optional<Task> findTask(int id) {
		Optional<Task> returnTask = repository.findById(id);
		if (returnTask.isPresent())
			return returnTask;
		return returnTask;
	}
	
	public void updateTask(Task updatedTask) {
//		Task searchedTask = repository.findById(id).get();
		Task task = repository.save(updatedTask);
	}
	
	public void removeTask(int id) {
		repository.deleteById(id);
	}

	public List<Task> findAllTasks() {
		List<Task> tasksList = repository.findAll();
		if (!(tasksList.isEmpty()))
			return tasksList;
		return tasksList;
	}
}
