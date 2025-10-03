package com.pdi_technologies.CRUD_presentation.contoller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdi_technologies.CRUD_presentation.domain.Todo;
import com.pdi_technologies.CRUD_presentation.repository.TodoRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerMockMvcTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll(); //clear the database before each test
    }

    @Test
    void testCreateTodo() throws Exception {

        //create a todo
        Todo todo = new Todo();
        todo.setTitle("Create Test Todo");
        todo.setCompleted(false);

        // post the todo
        mockMvc.perform(post("/api/todos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(todo)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.title").value("Create Test Todo"))
        .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void testGetAllTodos() throws Exception {
        //create a todo
        Todo todo = new Todo();
        todo.setTitle("Get All Test Todos");
        todo.setCompleted(false);
        todoRepository.save(todo);

        mockMvc.perform(get("/api/todos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Get All Test Todos"))
        .andExpect(jsonPath("$[0].completed").value(false));
    }

    @Test
    void testGetTodoById() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Get Todo By Id Test");
        todo.setCompleted(false);
        Todo savedTodo = todoRepository.save(todo);

        mockMvc.perform(get("/api/todos/{id}", savedTodo.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Get Todo By Id Test"))
        .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void testUpdateTodo() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Update Todo Test");
        todo.setCompleted(false);
        Todo savedTodo = todoRepository.save(todo);

        savedTodo.setTitle("Updated Todo Test");
        savedTodo.setCompleted(true);

        mockMvc.perform(put("/api/todos/{id}", savedTodo.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(savedTodo)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Updated Todo Test"))
        .andExpect(jsonPath("$.completed").value(true));
        
    }

    @Test
    void testDeleteTodo() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Delete Todo Test");
        todo.setCompleted(false);
        Todo savedTodo = todoRepository.save(todo);
        
        mockMvc.perform(delete("/api/todos/{id}", savedTodo.getId()))
        .andExpect(status().isNoContent());
    }

    @Test
    void testFilterByTitle() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Filter By Title Test");
        todo.setCompleted(false);
        todoRepository.save(todo);

        mockMvc.perform(get("/api/todos/title")
        .param("title", "title"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Filter By Title Test"));
    }

    @Test
    void testFilterByCompleted() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Filter By Completed Test");
        todo.setCompleted(true);
        todoRepository.save(todo);

        mockMvc.perform(get("/api/todos/completed")
        .param("completed", "true"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].completed").value(true));
    }
}
