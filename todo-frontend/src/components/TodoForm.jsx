import React, { useState, useEffect } from 'react';
import { createTodo, updateTodo } from '../services/todoService';

const TodoForm = ({ onTodoAdded, onTodoUpdated, editingTodo, onCancelEdit }) => {
  const [formData, setFormData] = useState({
    title: '',
    priority: 'medium',
    dueDate: ''
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  // Reset form when editingTodo changes
  useEffect(() => {
    if (editingTodo) {
      setFormData({
        title: editingTodo.title || '',
        priority: editingTodo.priority ? editingTodo.priority.toLowerCase() : 'medium',
        dueDate: editingTodo.dueDate ? editingTodo.dueDate.split('T')[0] : ''
      });
    } else {
      setFormData({
        title: '',
        priority: 'medium',
        dueDate: ''
      });
    }
    setError('');
  }, [editingTodo]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    // Clear error when user starts typing
    if (error) setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.title.trim()) {
      setError('Title is required');
      return;
    }

    setIsLoading(true);
    setError('');

    try {
      const todoData = {
        ...formData,
        title: formData.title.trim(),
        priority: formData.priority.toUpperCase(),
        dueDate: formData.dueDate || null
      };

      if (editingTodo) {
        // Update existing todo
        const updatedTodo = await updateTodo(editingTodo.id, todoData);
        onTodoUpdated(updatedTodo);
      } else {
        // Create new todo
        const newTodo = await createTodo(todoData);
        onTodoAdded(newTodo);
      }

      // Reset form
      setFormData({
        title: '',
        priority: 'medium',
        dueDate: ''
      });
    } catch (err) {
      setError(err.message || 'An error occurred. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancel = () => {
    setFormData({
      title: '',
      priority: 'medium',
      dueDate: ''
    });
    setError('');
    onCancelEdit();
  };

  return (
    <div className="card mb-4">
      <div className="card-header bg-primary text-white">
        <h5 className="mb-0">
          <i className="fas fa-plus-circle me-2"></i>
          {editingTodo ? 'Edit Todo' : 'Add New Todo'}
        </h5>
      </div>
      <div className="card-body">
        <form onSubmit={handleSubmit}>
          {error && (
            <div className="alert alert-danger" role="alert">
              <i className="fas fa-exclamation-triangle me-2"></i>
              {error}
            </div>
          )}

          <div className="row">
            <div className="col-md-8">
              <div className="mb-3">
                <label htmlFor="title" className="form-label">
                  <i className="fas fa-tasks me-1"></i>
                  Title *
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="title"
                  name="title"
                  value={formData.title}
                  onChange={handleInputChange}
                  placeholder="Enter todo title..."
                  disabled={isLoading}
                  required
                />
              </div>
            </div>
            <div className="col-md-4">
              <div className="mb-3">
                <label htmlFor="priority" className="form-label">
                  <i className="fas fa-flag me-1"></i>
                  Priority
                </label>
                <select
                  className="form-select"
                  id="priority"
                  name="priority"
                  value={formData.priority}
                  onChange={handleInputChange}
                  disabled={isLoading}
                >
                  <option value="low">Low</option>
                  <option value="medium">Medium</option>
                  <option value="high">High</option>
                  <option value="urgent">Urgent</option>
                </select>
              </div>
            </div>
          </div>


          <div className="row">
            <div className="col-md-6">
              <div className="mb-3">
                <label htmlFor="dueDate" className="form-label">
                  <i className="fas fa-calendar me-1"></i>
                  Due Date
                </label>
                <input
                  type="date"
                  className="form-control"
                  id="dueDate"
                  name="dueDate"
                  value={formData.dueDate}
                  onChange={handleInputChange}
                  disabled={isLoading}
                />
              </div>
            </div>
          </div>

          <div className="d-flex gap-2">
            <button
              type="submit"
              className="btn btn-add"
              disabled={isLoading || !formData.title.trim()}
            >
              {isLoading ? (
                <>
                  <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                  {editingTodo ? 'Updating...' : 'Adding...'}
                </>
              ) : (
                <>
                  <i className={`fas ${editingTodo ? 'fa-save' : 'fa-plus'} me-2`}></i>
                  {editingTodo ? 'Update Todo' : 'Add Todo'}
                </>
              )}
            </button>
            
            {editingTodo && (
              <button
                type="button"
                className="btn btn-outline-secondary"
                onClick={handleCancel}
                disabled={isLoading}
              >
                <i className="fas fa-times me-2"></i>
                Cancel
              </button>
            )}
          </div>
        </form>
      </div>
    </div>
  );
};

export default TodoForm;

