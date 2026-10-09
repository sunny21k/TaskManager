package com.example.taskmanager.repository;

import com.example.taskmanager.enums.Category;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository  extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);
    List<Task> findByUserAndCategory(User user, Category category);
}
