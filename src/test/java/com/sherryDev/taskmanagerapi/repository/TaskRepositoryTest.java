package com.sherryDev.taskmanagerapi.repository;

import com.sherryDev.taskmanagerapi.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TaskRepositoryTest {

    @Autowired
    TaskRepository repository;

    @Test
    @DirtiesContext
    void  shouldSaveAndReturnSavedTask() {
        // create a Task
        Task task = new Task();
        task.setTName("Tidy bedroom");
        task.setTDescription("Make your bed and clean floor and bedsheets");
        
        // save task to database
        Task savedTask = repository.save(task);

        //make assertions based on returned saved task
        assertThat(savedTask).isEqualTo(task);
    }

    @Test
    void shouldSaveMultipleTasks() {
        //create multiple tasks
        //Task 1
        Task task1 = new Task();
        task1.setTName("Tidy bedroom");
        task1.setTDescription("Make your bed and clean floor and bedsheets");

        //Task 2
        Task task2 = new Task();
        task2.setTName("Tidy Kitchen");
        task2.setTDescription("Wash the dishes and clean the kitchen floor, tidy the lounge as well");

        //save multiple tasks to database

        repository.saveAll(List.of(task1,task2));

    }

    @Test
    void shouldFindTaskWithProvidedId() {
        //create Id
        //provide relevant Id
        int id = 1;

        //retrieve task with relevant Id
        Optional<Task> taskFound = repository.findById(id);

        //make assertions based on Id of returned task
        assertThat(taskFound.get().getTId()).isEqualTo(id);
    }

    @Test
    void shouldUpdateTaskAndReturnUpdatedTask() {
        //retrieve existing Task
        //provide relevant Id
        int id = 9;
        Optional<Task> taskFound = repository.findById(id);
        Task task = taskFound.get();

        //update existing task using getter and setter
        task.setTName("Clean the cars");
        task.setTDescription("Clean both exterior and interior of daily driven cars on premises");

        //update database using repository
        Task updatedTask = repository.save(task);

        //make assertions based on results in database and updated content
        assertThat(updatedTask.getTName()).isEqualTo("Clean the cars");
        assertThat(updatedTask.getTDescription()).isEqualTo("Clean both exterior and interior of daily driven cars on premises");
    }

    @Test
    void shouldReturnAllTasks() {
        List<Task> tasks = repository.findAll();
        tasks.forEach((task) -> {
            System.out.println(task.toString());
        });
    }

    @Test
    void shouldRemoveTaskById() {
        int id = 1;
        repository.deleteById(id);
    }

    @Test
    void shouldRemoveTaskByEntity() {
        //Retrieve task to be deleted from database
        //provide relevant Id
        Task deleteTask = repository.findById(7).get();

        //Delete retrieved task
        repository.delete(deleteTask);
    }

    @Test
    void shouldRemoveAllTasks() {
        repository.deleteAll();
    }

    @Test
    void shouldReturnNumberOfSavedEntities() {
        long numEntities = repository.count();
        System.out.println("The number of saved entities: " + numEntities);
    }

    @Test
    void shouldCheckTaskExistenceByIdInDatabase() {

        //provide relevant Id
        int id = 8;
        boolean bTaskExist = repository.existsById(id);

        assertThat(bTaskExist).isTrue();
    }
}
