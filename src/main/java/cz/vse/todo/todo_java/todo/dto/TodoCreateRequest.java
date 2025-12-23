package cz.vse.todo.todo_java.todo.dto;

import jakarta.validation.constraints.NotBlank;

public class TodoCreateRequest {
    @NotBlank
    private String title;

    private String status;

    public String getTitle() { return title; }
    public String getStatus() { return status; }

    public void setTitle(String title) { this.title = title; }
    public void setStatus(String status) { this.status = status; }
}
