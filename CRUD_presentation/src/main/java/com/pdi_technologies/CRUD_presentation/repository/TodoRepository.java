package com.pdi_technologies.CRUD_presentation.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pdi_technologies.CRUD_presentation.domain.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    
    List<Todo> findByCompleted(boolean completed);
    
    List<Todo> findByTitleContaining(String title);
    
    List<Todo> findByPriority(Todo.Priority priority);
    
    List<Todo> findByDueDate(LocalDate dueDate);
    
    List<Todo> findByDueDateBefore(LocalDate date);
    
    List<Todo> findByDueDateAfter(LocalDate date);
    
    List<Todo> findByDueDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT t FROM Todo t WHERE t.priority = :priority AND t.completed = :completed")
    List<Todo> findByPriorityAndCompleted(@Param("priority") Todo.Priority priority, @Param("completed") boolean completed);
    
    @Query("SELECT t FROM Todo t WHERE t.dueDate <= :date AND t.completed = false ORDER BY t.priority DESC, t.dueDate ASC")
    List<Todo> findOverdueTodos(@Param("date") LocalDate date);
    
    @Query("SELECT t FROM Todo t WHERE t.dueDate BETWEEN :startDate AND :endDate ORDER BY t.priority DESC, t.dueDate ASC")
    List<Todo> findTodosByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
