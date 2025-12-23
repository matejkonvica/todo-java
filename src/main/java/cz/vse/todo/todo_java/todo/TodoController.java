package cz.vse.todo.todo_java.todo;

import cz.vse.todo.todo_java.todo.dto.TodoCreateRequest;
import cz.vse.todo.todo_java.todo.dto.TodoUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {

    private final TodoRepo repo;

    public TodoController(TodoRepo repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<TodoItem> list() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<TodoItem> create(@Valid @RequestBody TodoCreateRequest req) {
        TodoStatus status = parseStatusOrDefault(req.getStatus(), TodoStatus.NOT_FINISHED);
        TodoItem saved = repo.save(new TodoItem(req.getTitle(), status));
        return ResponseEntity.created(URI.create("/todo/" + saved.getId())).body(saved);
    }

    @GetMapping("/{id}")
    public TodoItem get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
    }

    @PutMapping("/{id}")
    public TodoItem update(@PathVariable Long id, @Valid @RequestBody TodoUpdateRequest req) {
        TodoItem item = repo.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        item.setTitle(req.getTitle());
        if (req.getStatus() != null) {
            item.setStatus(parseStatusOrThrow(req.getStatus()));
        }
        return repo.save(item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) throw new TodoNotFoundException(id);
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private static TodoStatus parseStatusOrDefault(String value, TodoStatus def) {
        if (value == null || value.trim().isEmpty()) return def;
        return parseStatusOrThrow(value);
    }

    private static TodoStatus parseStatusOrThrow(String value) {
        try {
            return TodoStatus.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid status. Allowed: NOT_FINISHED, ONGOING, FINISHED");
        }
    }
}
