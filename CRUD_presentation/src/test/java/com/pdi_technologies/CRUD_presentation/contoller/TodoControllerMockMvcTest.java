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

import java.time.LocalDate;

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

    // Priority-based filter tests
    @Test
    void testFilterByPriority() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("High Priority Test");
        todo.setCompleted(false);
        todo.setPriority(Todo.Priority.HIGH);
        todoRepository.save(todo);

        mockMvc.perform(get("/api/todos/priority")
        .param("priority", "HIGH"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].priority").value("HIGH"))
        .andExpect(jsonPath("$[0].title").value("High Priority Test"));
    }

    @Test
    void testFilterByPriorityAndCompleted() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Urgent Completed Test");
        todo.setCompleted(true);
        todo.setPriority(Todo.Priority.URGENT);
        todoRepository.save(todo);

        mockMvc.perform(get("/api/todos/priority-completed")
        .param("priority", "URGENT")
        .param("completed", "true"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].priority").value("URGENT"))
        .andExpect(jsonPath("$[0].completed").value(true))
        .andExpect(jsonPath("$[0].title").value("Urgent Completed Test"));
    }

    // Due date filter tests
    @Test
    void testFilterByDueDate() throws Exception {
        LocalDate testDate = LocalDate.now().plusDays(1);
        Todo todo = new Todo();
        todo.setTitle("Due Date Test");
        todo.setCompleted(false);
        todo.setDueDate(testDate);
        todoRepository.save(todo);

        mockMvc.perform(get("/api/todos/due-date")
        .param("dueDate", testDate.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].dueDate").value(testDate.toString()))
        .andExpect(jsonPath("$[0].title").value("Due Date Test"));
    }

    @Test
    void testFilterByDueBefore() throws Exception {
        LocalDate testDate = LocalDate.now().plusDays(2);
        Todo todo = new Todo();
        todo.setTitle("Due Before Test");
        todo.setCompleted(false);
        todo.setDueDate(LocalDate.now().plusDays(1));
        todoRepository.save(todo);

        mockMvc.perform(get("/api/todos/due-before")
        .param("date", testDate.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Due Before Test"));
    }

    @Test
    void testFilterByDueAfter() throws Exception {
        LocalDate testDate = LocalDate.now().minusDays(1);
        Todo todo = new Todo();
        todo.setTitle("Due After Test");
        todo.setCompleted(false);
        todo.setDueDate(LocalDate.now().plusDays(1));
        todoRepository.save(todo);

        mockMvc.perform(get("/api/todos/due-after")
        .param("date", testDate.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Due After Test"));
    }

    @Test
    void testFilterByDateRange() throws Exception {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(3);
        Todo todo = new Todo();
        todo.setTitle("Date Range Test");
        todo.setCompleted(false);
        todo.setDueDate(LocalDate.now().plusDays(1));
        todoRepository.save(todo);

        mockMvc.perform(get("/api/todos/due-range")
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Date Range Test"));
    }

    @Test
    void testGetOverdueTodos() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Overdue Test");
        todo.setCompleted(false);
        todo.setDueDate(LocalDate.now().minusDays(1));
        todoRepository.save(todo);

        mockMvc.perform(get("/api/todos/overdue"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Overdue Test"))
        .andExpect(jsonPath("$[0].completed").value(false));
    }

    // Combined filter tests
    @Test
    void testFilterByPriorityAndDueDate() throws Exception {
        LocalDate testDate = LocalDate.now().plusDays(1);
        Todo todo = new Todo();
        todo.setTitle("Priority Due Date Test");
        todo.setCompleted(false);
        todo.setPriority(Todo.Priority.MEDIUM);
        todo.setDueDate(testDate);
        todoRepository.save(todo);

        mockMvc.perform(get("/api/todos/priority-due-date")
        .param("priority", "MEDIUM")
        .param("dueDate", testDate.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].priority").value("MEDIUM"))
        .andExpect(jsonPath("$[0].dueDate").value(testDate.toString()))
        .andExpect(jsonPath("$[0].title").value("Priority Due Date Test"));
    }

    @Test
    void testGetTodosSortedByPriorityAndDueDate() throws Exception {
        // Create todos with different priorities and due dates
        Todo urgentTodo = new Todo();
        urgentTodo.setTitle("Urgent Todo");
        urgentTodo.setCompleted(false);
        urgentTodo.setPriority(Todo.Priority.URGENT);
        urgentTodo.setDueDate(LocalDate.now().plusDays(1));
        todoRepository.save(urgentTodo);

        Todo lowTodo = new Todo();
        lowTodo.setTitle("Low Priority Todo");
        lowTodo.setCompleted(false);
        lowTodo.setPriority(Todo.Priority.LOW);
        lowTodo.setDueDate(LocalDate.now().plusDays(2));
        todoRepository.save(lowTodo);

        Todo highTodo = new Todo();
        highTodo.setTitle("High Priority Todo");
        highTodo.setCompleted(false);
        highTodo.setPriority(Todo.Priority.HIGH);
        highTodo.setDueDate(LocalDate.now().plusDays(1));
        todoRepository.save(highTodo);

        mockMvc.perform(get("/api/todos/sorted"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].priority").value("URGENT"))
        .andExpect(jsonPath("$[1].priority").value("HIGH"))
        .andExpect(jsonPath("$[2].priority").value("LOW"));
    }

    // Test creating todo with priority and due date
    @Test
    void testCreateTodoWithPriorityAndDueDate() throws Exception {
        LocalDate testDate = LocalDate.now().plusDays(1);
        Todo todo = new Todo();
        todo.setTitle("Create Todo With Priority Test");
        todo.setCompleted(false);
        todo.setPriority(Todo.Priority.HIGH);
        todo.setDueDate(testDate);

        mockMvc.perform(post("/api/todos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(todo)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.title").value("Create Todo With Priority Test"))
        .andExpect(jsonPath("$.completed").value(false))
        .andExpect(jsonPath("$.priority").value("HIGH"))
        .andExpect(jsonPath("$.dueDate").value(testDate.toString()));
    }

    // Test updating todo with priority and due date
    @Test
    void testUpdateTodoWithPriorityAndDueDate() throws Exception {
        LocalDate originalDate = LocalDate.now().plusDays(1);
        LocalDate updatedDate = LocalDate.now().plusDays(2);
        
        Todo todo = new Todo();
        todo.setTitle("Update Priority Test");
        todo.setCompleted(false);
        todo.setPriority(Todo.Priority.LOW);
        todo.setDueDate(originalDate);
        Todo savedTodo = todoRepository.save(todo);

        savedTodo.setTitle("Updated Priority Test");
        savedTodo.setCompleted(true);
        savedTodo.setPriority(Todo.Priority.URGENT);
        savedTodo.setDueDate(updatedDate);

        mockMvc.perform(put("/api/todos/{id}", savedTodo.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(savedTodo)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Updated Priority Test"))
        .andExpect(jsonPath("$.completed").value(true))
        .andExpect(jsonPath("$.priority").value("URGENT"))
        .andExpect(jsonPath("$.dueDate").value(updatedDate.toString()));
    }
}
