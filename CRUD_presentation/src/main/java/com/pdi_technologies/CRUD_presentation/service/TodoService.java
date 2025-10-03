package com.pdi_technologies.CRUD_presentation.service;

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

}
