package ch.martinelli.demoweb.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @PostConstruct
    public void init() {
        todoRepository.saveAll(List.of(
                new Todo("Blumen giessen"),
                new Todo("Rasen mähen"),
                new Todo("Einkaufen"))
        );
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Todo todo) {
        if (todo.getId() != null) {
            return ResponseEntity.badRequest().build();
        } else {
            todoRepository.save(todo);
            return ResponseEntity.created(URI.create(todo.getId().toString())).build();
        }
    }

    @GetMapping
    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Todo> get(int id) {
        return todoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            todoRepository.deleteById(id);
            throw new RuntimeException();
        }
    }
}
