package cz.vse.todo.todo_java.todo;

import jakarta.persistence.*;

@Entity
@Table(name = "todo_item")
public class TodoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status = TodoStatus.NOT_FINISHED;

    public TodoItem() {}

    public TodoItem(String title, TodoStatus status) {
        this.title = title;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public TodoStatus getStatus() { return status; }

    public void setTitle(String title) { this.title = title; }
    public void setStatus(TodoStatus status) { this.status = status; }
}
