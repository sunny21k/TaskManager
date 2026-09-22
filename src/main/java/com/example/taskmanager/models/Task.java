package com.example.taskmanager.models;

import com.example.taskmanager.enums.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Tasks")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    private boolean isCompleted = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "users-tasks")
    private User user;
}
