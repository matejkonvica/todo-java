package cz.vse.todo.todo_java.todo;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(Long id) {
        super("Todo item not found: " + id);
    }
}
