package com.sherryDev.taskmanagerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.sherryDev.taskmanagerapi.model.Task;


//no need to add @Repository annotation because simplejpa which uses @Repository annotation is a implementation of JpaRepository if you extended CrudRepository OR Repository you must annotate repository but since JpaRespository extends CrudRepository no need.
//add extends JpaRepository<Task,Integer> OR CrudRepository<Task,Integer>


public interface TaskRepository extends JpaRepository<Task,Integer> {

}
