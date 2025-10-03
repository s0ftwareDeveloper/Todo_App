package com.pdi_technologies.CRUD_presentation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pdi_technologies.CRUD_presentation.domain.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    
    List<Todo> findByCompleted(boolean completed);
    
    List<Todo> findByTitleContaining(String title);
}
