import React, { useState, useEffect, useCallback } from 'react';
import { getTodos, updateTodo, deleteTodo } from '../services/todoService';

const TodoList = ({ refreshTrigger, onEditTodo }) => {
  const [todos, setTodos] = useState([]);
  const [filteredTodos, setFilteredTodos] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [filterPriority, setFilterPriority] = useState('all');
  const [filterStatus, setFilterStatus] = useState('all');
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');
  const [sortBy, setSortBy] = useState('createdAt');
  const [sortOrder, setSortOrder] = useState('desc');

  // Load todos when refreshTrigger changes
  useEffect(() => {
    loadTodos();
  }, [refreshTrigger]);

  const loadTodos = async () => {
    setIsLoading(true);
    setError(''); // clears any existing error messages
    
    try {
      const todosData = await getTodos();
      setTodos(todosData); // sets the todos data to the state
    } catch (err) {
      console.error('Error loading todos:', err);
      setError(err.message || 'Failed to load todos');
    } finally {
      setIsLoading(false);
    }
  };

  const filterAndSortTodos = useCallback(() => {
    let filtered = [...todos];

    // filters the todos by the search query
    if (searchQuery.trim()) {
      const query = searchQuery.toLowerCase();
      filtered = filtered.filter(todo =>
        todo.title.toLowerCase().includes(query)
      );
    }

    // filters the todos by the priority
    if (filterPriority !== 'all') {
      filtered = filtered.filter(todo => todo.priority.toLowerCase() === filterPriority);
    }

    // filters the todos by the status
    if (filterStatus !== 'all') {
      const isCompleted = filterStatus === 'completed';
      filtered = filtered.filter(todo => todo.completed === isCompleted);
    }

    // Apply sorting
    filtered.sort((a, b) => {
      let aValue, bValue;
      
      switch (sortBy) {
        case 'title': {
          aValue = a.title.toLowerCase();
          bValue = b.title.toLowerCase();
          break;
        }
        case 'priority': {
          const priorityOrder = { urgent: 4, high: 3, medium: 2, low: 1 };
          aValue = priorityOrder[a.priority] || 0;
          bValue = priorityOrder[b.priority] || 0;
          break;
        }
        case 'dueDate': {
          aValue = new Date(a.dueDate || '9999-12-31');
          bValue = new Date(b.dueDate || '9999-12-31');
          break;
        }
        case 'createdAt':
        default: {
          aValue = new Date(a.createdAt || a.id);
          bValue = new Date(b.createdAt || b.id);
          break;
        }
      }

      if (sortOrder === 'asc') {
        return aValue > bValue ? 1 : -1;
      } else {
        return aValue < bValue ? 1 : -1;
      }
    });

    setFilteredTodos(filtered);
  }, [todos, searchQuery, filterPriority, filterStatus, sortBy, sortOrder]);

  // Filter and sort todos when todos, searchQuery, or filters change
  useEffect(() => {
    filterAndSortTodos();
  }, [filterAndSortTodos]);

  const handleToggleComplete = async (todo) => {
    try {
      const updatedTodo = await updateTodo(todo.id, {
        ...todo,
        completed: !todo.completed
      });
      
      setTodos(prevTodos =>
        prevTodos.map(t =>
          t.id === todo.id ? updatedTodo : t
        )
      );
    } catch (err) {
      setError(err.message || 'Failed to update todo');
      console.error('Error updating todo:', err);
    }
  };

  const handleDeleteTodo = async (todoId) => {
    if (window.confirm('Are you sure you want to delete this todo?')) {
      try {
        await deleteTodo(todoId);
        setTodos(prevTodos => prevTodos.filter(t => t.id !== todoId));
      } catch (err) {
        setError(err.message || 'Failed to delete todo');
        console.error('Error deleting todo:', err);
      }
    }
  };

  const handleEditTodo = (todo) => {
    onEditTodo(todo);
  };

  const getPriorityBadgeClass = (priority) => {
    switch (priority.toLowerCase()) {
      case 'urgent':
        return 'badge bg-danger'; // Red - highest urgency
      case 'high':
        return 'badge bg-warning text-dark'; // Orange - high urgency
      case 'medium':
        return 'badge bg-info'; // Light blue - medium urgency
      case 'low':
        return 'badge bg-primary'; // Blue - low urgency
      default:
        return 'badge bg-secondary';
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'No due date';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short', // for readable month names
      day: 'numeric'
    });
  };

  const isOverdue = (dueDate) => {
    if (!dueDate) return false;
    return new Date(dueDate) < new Date() && new Date(dueDate).toDateString() !== new Date().toDateString();
  };

  if (isLoading) {
    return (
      <div className="text-center py-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-3 text-muted">Loading todos...</p>
      </div>
    );
  }

  return (
    <div className="card">
      <div className="card-header bg-info text-white">
        <h5 className="mb-0">
          <i className="fas fa-list me-2"></i>
          Todo List ({filteredTodos.length} items)
        </h5>
      </div>
      <div className="card-body">
        {error && (
          <div className="alert alert-danger" role="alert">
            {error}
            <button
              type="button"
              className="btn-close"
              onClick={() => setError('')}
              aria-label="Close"
            ></button>
          </div>
        )}

        {/* Search and Filter Controls */}
        <div className="row mb-4">
          <div className="col-md-6">
            <div className="search-container">
              <input
                type="text"
                className="form-control"
                placeholder="Search todos..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
              />
              <i className="fas fa-search search-icon"></i>
            </div>
          </div>
          <div className="col-md-2">
            <select
              className="form-select"
              value={filterPriority}
              onChange={(e) => setFilterPriority(e.target.value)}
            >
              <option value="all">All Priorities</option>
              <option value="urgent">Urgent</option>
              <option value="high">High</option>
              <option value="medium">Medium</option>
              <option value="low">Low</option>
            </select>
          </div>
          <div className="col-md-2">
            <select
              className="form-select"
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
            >
              <option value="all">All Status</option>
              <option value="pending">Pending</option>
              <option value="completed">Completed</option>
            </select>
          </div>
          <div className="col-md-2">
            <select
              className="form-select"
              value={`${sortBy}-${sortOrder}`}
              onChange={(e) => {
                const [field, order] = e.target.value.split('-');
                setSortBy(field);
                setSortOrder(order);
              }}
            >
              <option value="createdAt-desc">Newest First</option>
              <option value="createdAt-asc">Oldest First</option>
              <option value="title-asc">Title A-Z</option>
              <option value="title-desc">Title Z-A</option>
              <option value="priority-desc">Priority High-Low</option>
              <option value="priority-asc">Priority Low-High</option>
              <option value="dueDate-asc">Due Date Soon</option>
              <option value="dueDate-desc">Due Date Later</option>
            </select>
          </div>
        </div>

        {/* Todo List */}
        {filteredTodos.length === 0 ? (
          <div className="empty-state">
            <i className="fas fa-clipboard-list"></i>
            <h4>No todos found</h4>
            <p>
              {searchQuery || filterPriority !== 'all' || filterStatus !== 'all'
                ? 'Try adjusting your search or filters'
                : 'Nothing to do?!'
              }
            </p>
          </div>
        ) : (
          <div className="todo-list">
            {filteredTodos.map((todo) => (
              <div
                key={todo.id}
                className={`todo-item p-3 ${todo.completed ? 'completed' : ''}`}
              >
                <div className="row align-items-center">
                  <div className="col-md-1">
                    <div className="form-check">
                      <input
                        className="form-check-input"
                        type="checkbox"
                        checked={todo.completed}
                        onChange={() => handleToggleComplete(todo)}
                        id={`todo-${todo.id}`}
                      />
                      <label className="form-check-label" htmlFor={`todo-${todo.id}`}>
                        <span className="visually-hidden">Toggle completion</span>
                      </label>
                    </div>
                  </div>
                  <div className="col-md-6">
                    <h6 className={`mb-1 todo-text ${todo.completed ? 'text-decoration-line-through' : ''}`}>
                      {todo.title}
                    </h6>
                    <div className="d-flex gap-2 align-items-center">
                      <span className={getPriorityBadgeClass(todo.priority)}>
                        {todo.priority.toUpperCase()}
                      </span>
                      {todo.dueDate && (
                        <small className={`text-muted ${isOverdue(todo.dueDate) && !todo.completed ? 'text-danger fw-bold' : ''}`}>
                          <i className="fas fa-calendar me-1"></i>
                          {formatDate(todo.dueDate)}
                          {isOverdue(todo.dueDate) && !todo.completed && ' (Overdue)'}
                        </small>
                      )}
                    </div>
                  </div>
                  <div className="col-md-5 text-end">
                    <div className="btn-group" role="group">
                      <button
                        type="button"
                        className="btn btn-edit btn-sm"
                        onClick={() => handleEditTodo(todo)}
                        title="Edit todo"
                      >
                        <i className="fas fa-edit"></i>
                      </button>
                      <button
                        type="button"
                        className="btn btn-delete btn-sm"
                        onClick={() => handleDeleteTodo(todo.id)}
                        title="Delete todo"
                      >
                        <i className="fas fa-trash"></i>
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default TodoList;