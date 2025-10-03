import React, { useState } from 'react';
import TodoForm from './components/TodoForm';
import TodoList from './components/TodoList';
import './App.css';

function App() {
  const [editingTodo, setEditingTodo] = useState(null);
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  const handleTodoAdded = (newTodo) => {
    console.log('Todo added:', newTodo);
    setRefreshTrigger(prev => prev + 1);
  };

  const handleTodoUpdated = (updatedTodo) => {
    console.log('Todo updated:', updatedTodo);
    setEditingTodo(null);
    setRefreshTrigger(prev => prev + 1);
  };

  const handleEditTodo = (todo) => {
    setEditingTodo(todo);
  };

  const handleCancelEdit = () => {
    setEditingTodo(null);
  };

  return (
    <div className="todo-app">
      <div className="container-fluid">
        <div className="row justify-content-center">
          <div className="col-12 col-lg-10 col-xl-8">
              {/* Header */}
              <div className="text-center mb-5">
                <h1 className="display-4 fw-bold text-primary mb-3">
                  <i className="fas fa-tasks me-3"></i>
                  Todo List
                </h1>
              </div>

              {/* Todo Form */}
              <TodoForm
                onTodoAdded={handleTodoAdded}
                onTodoUpdated={handleTodoUpdated}
                editingTodo={editingTodo}
                onCancelEdit={handleCancelEdit}
              />

              {/* Todo List */}
              <TodoList
                refreshTrigger={refreshTrigger}
                onEditTodo={handleEditTodo}
              />
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;