package com.example.taskmanager.service.implementation;

import com.example.taskmanager.dto.ApiResponse;
import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.enums.Category;
import com.example.taskmanager.exceptions.NotFoundException;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.models.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TaskServiceImplementation implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ApiResponse<TaskDTO> createTask(TaskDTO taskRequest, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found."));

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setCategory(taskRequest.getCategory() != null ? taskRequest.getCategory() : Category.PERSONAL);
        task.setUser(user);

        Task savedTasks = taskRepository.save(task);
        TaskDTO savedTasksDTO = mapTaskToTaskDTO(savedTasks);

        return new ApiResponse<>(201, "Task created successfully", savedTasksDTO);

    }

    @Override
    public ApiResponse<List<TaskDTO>> getTaskByUserAndCategory(String email, Category category) {
        return null;
    }

    @Override
    public ApiResponse<String> deleteTask(Long id) {
        return null;
    }

    @Override
    public ApiResponse<TaskDTO> toggleTaskCompletion(Long id, String email) {
        return null;
    }

    private TaskDTO mapTaskToTaskDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(taskDTO.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setCategory(task.getCategory());
        taskDTO.setCompleted(taskDTO.isCompleted());
        return taskDTO;
    }
}
