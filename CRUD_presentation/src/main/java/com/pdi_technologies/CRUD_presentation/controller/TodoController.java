package com.pdi_technologies.CRUD_presentation.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pdi_technologies.CRUD_presentation.domain.Todo;
import com.pdi_technologies.CRUD_presentation.service.TodoService;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:5173")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    
    // Create
    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        return ResponseEntity.ok(todoService.saveTodo(todo));
    }

    // Read
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    // Read by id
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTodoById(id).orElse(null));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        return ResponseEntity.ok(todoService.getTodoById(id)
        .map(existingTodo -> {
            existingTodo.setTitle(todo.getTitle());
            existingTodo.setCompleted(todo.isCompleted());
            existingTodo.setPriority(todo.getPriority());
            existingTodo.setDueDate(todo.getDueDate());
            return todoService.saveTodo(existingTodo);
        }).orElse(null));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    // Filter by completed status
    @GetMapping("/completed")
    public ResponseEntity<List<Todo>> filterByCompleted(@RequestParam boolean completed) {
        return ResponseEntity.ok(todoService.getCompletedTodos(completed));
    }

    // Filter by title
    @GetMapping("/title")
    public ResponseEntity<List<Todo>> filterByTitle(@RequestParam String title) {
        return ResponseEntity.ok(todoService.getTodosByTitle(title));
    }

    // Priority-based endpoints
    @GetMapping("/priority")
    public ResponseEntity<List<Todo>> filterByPriority(@RequestParam Todo.Priority priority) {
        return ResponseEntity.ok(todoService.getTodosByPriority(priority));
    }

    @GetMapping("/priority-completed")
    public ResponseEntity<List<Todo>> filterByPriorityAndCompleted(
            @RequestParam Todo.Priority priority, 
            @RequestParam boolean completed) {
        return ResponseEntity.ok(todoService.getTodosByPriorityAndCompleted(priority, completed));
    }

    // Due date endpoints
    @GetMapping("/due-date")
    public ResponseEntity<List<Todo>> filterByDueDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate) {
        return ResponseEntity.ok(todoService.getTodosByDueDate(dueDate));
    }

    @GetMapping("/due-before")
    public ResponseEntity<List<Todo>> filterByDueBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(todoService.getTodosDueBefore(date));
    }

    @GetMapping("/due-after")
    public ResponseEntity<List<Todo>> filterByDueAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(todoService.getTodosDueAfter(date));
    }

    @GetMapping("/due-range")
    public ResponseEntity<List<Todo>> filterByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(todoService.getTodosByDateRange(startDate, endDate));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Todo>> getOverdueTodos() {
        return ResponseEntity.ok(todoService.getOverdueTodos());
    }

    // Combined filtering
    @GetMapping("/priority-due-date")
    public ResponseEntity<List<Todo>> filterByPriorityAndDueDate(
            @RequestParam Todo.Priority priority,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate) {
        return ResponseEntity.ok(todoService.getTodosByPriorityAndDueDate(priority, dueDate));
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<Todo>> getTodosSortedByPriorityAndDueDate() {
        return ResponseEntity.ok(todoService.getTodosSortedByPriorityAndDueDate());
    }
}
