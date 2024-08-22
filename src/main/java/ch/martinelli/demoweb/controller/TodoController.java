package ch.martinelli.demoweb.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequestMapping("todos")
@RestController
public class TodoController {

    private final List<Todo> todos = new ArrayList<>();

    @PostConstruct
    public void init() {
        todos.add(new Todo(1, "Blumen giessen"));
        todos.add(new Todo(2, "Rasen mähen"));
        todos.add(new Todo(3, "Einkaufen"));
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Todo todo) {
        if (todo.getId() != null) {
            return ResponseEntity.badRequest().build();
        } else {
            int id = todos.stream().max(Comparator.comparing(Todo::getId)).map(Todo::getId).orElse(0);
            todo.setId(++id);
            todos.add(todo);
            return ResponseEntity.created(URI.create(todo.getId().toString())).build();
        }
    }

    @GetMapping
    public List<Todo> getAll() {
        return todos;
    }

    @GetMapping("{id}")
    public ResponseEntity<Todo> get(int id) {
        return todos.stream().filter(todo -> todo.getId() == id)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        Optional<Todo> optionalTodo = todos.stream().filter(todo -> todo.getId() == id).findFirst();
        if (optionalTodo.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            todos.remove(optionalTodo.get());
            return ResponseEntity.ok().build();
        }
    }
}
