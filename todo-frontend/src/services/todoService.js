import axios from 'axios';

const API_URL = 'http://localhost:8080/api/todos';

const api = axios.create({
  baseURL: API_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// API functions with error handling
export const getTodos = async () => {
  try {
    const response = await api.get('');
    return response.data;
  } catch (error) {
    console.error('Error fetching todos:', error);
    throw new Error('Failed to fetch todos. Please check if the backend server is running.');
  }
};

export const createTodo = async (todo) => {
  try {
    const response = await api.post('', todo);
    return response.data;
  } catch (error) {
    console.error('Error creating todo:', error);
    throw new Error('Failed to create todo. Please try again.');
  }
};

export const updateTodo = async (id, todo) => {
  try {
    const response = await api.put(`/${id}`, todo);
    return response.data;
  } catch (error) {
    console.error('Error updating todo:', error);
    throw new Error('Failed to update todo. Please try again.');
  }
};

export const deleteTodo = async (id) => {
  try {
    const response = await api.delete(`/${id}`);
    return response.data;
  } catch (error) {
    console.error('Error deleting todo:', error);
    throw new Error('Failed to delete todo. Please try again.');
  }
};

// Search todos function
export const searchTodos = async (query) => {
  try {
    const response = await api.get(`/search?q=${encodeURIComponent(query)}`);
    return response.data;
  } catch (error) {
    console.error('Error searching todos:', error);
    throw new Error('Failed to search todos. Please try again.');
  }
};