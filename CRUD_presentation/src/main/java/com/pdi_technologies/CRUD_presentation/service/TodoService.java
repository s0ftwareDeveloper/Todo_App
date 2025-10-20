package com.pdi_technologies.CRUD_presentation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pdi_technologies.CRUD_presentation.domain.Todo;
import com.pdi_technologies.CRUD_presentation.repository.TodoRepository;

@Service
public class TodoService {
    
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // create or update a todo
    public Todo saveTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    // read all todos
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // read a todo by id
    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    // delete a todo
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

    // filter todos by completed status
    public List<Todo> getCompletedTodos(boolean completed) {
        return todoRepository.findByCompleted(completed);
    }

    public List<Todo> getTodosByTitle(String title) {
        return todoRepository.findByTitleContaining(title);
    }

    // Priority-based methods
    public List<Todo> getTodosByPriority(Todo.Priority priority) {
        return todoRepository.findByPriority(priority);
    }

    public List<Todo> getTodosByPriorityAndCompleted(Todo.Priority priority, boolean completed) {
        return todoRepository.findByPriorityAndCompleted(priority, completed);
    }

    // Due date methods
    public List<Todo> getTodosByDueDate(LocalDate dueDate) {
        return todoRepository.findByDueDate(dueDate);
    }

    public List<Todo> getTodosDueBefore(LocalDate date) {
        return todoRepository.findByDueDateBefore(date);
    }

    public List<Todo> getTodosDueAfter(LocalDate date) {
        return todoRepository.findByDueDateAfter(date);
    }

    public List<Todo> getTodosByDateRange(LocalDate startDate, LocalDate endDate) {
        return todoRepository.findTodosByDateRange(startDate, endDate);
    }

    public List<Todo> getOverdueTodos() {
        return todoRepository.findOverdueTodos(LocalDate.now());
    }

    // Combined filtering methods
    public List<Todo> getTodosByPriorityAndDueDate(Todo.Priority priority, LocalDate dueDate) {
        return todoRepository.findByPriority(priority).stream()
                .filter(todo -> todo.getDueDate() != null && todo.getDueDate().equals(dueDate))
                .toList();
    }


}
