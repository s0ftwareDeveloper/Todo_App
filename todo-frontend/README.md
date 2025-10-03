# Todo Manager - CRUD Operations

A modern, responsive todo management application built with React and Bootstrap, featuring full CRUD operations and advanced search functionality.

## Features

### Core Functionality
- **Create** new todos with title, description, priority, and due date
- **Read** and display todos with nice looking UI
- **Update** existing todos (edit mode)
- **Delete** todos with confirmation

### Advanced Search & Filtering
- Search by title and description
- Filter by Priority (High, Medium, Low)
- Filter by Status (All, Pending, Completed)
- Various Sorting options:
  - Newest/Oldest first
  - Title A-Z/Z-A
  - Priority High-Low/Low-High
  - Due Date Soon/Later

### Modern UI/UX
- Bootstrap 5 for responsive design
- Font awesome icons for better visual experience
- Smooth animations
- Responsive layout
- Accessibility features with proper ARIA labels

### Technical Features
- Axios for API communication
- Error handling with user-friendly messages
- Loading states with spinners
- Form validation

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd todo-frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm run dev
   ```

4. **Open your browser**
   Navigate to `http://localhost:5173`

## API Endpoints

The application expects the following API endpoints:

- `GET /api/todos` - Fetch all todos
- `POST /api/todos` - Create a new todo
- `PUT /api/todos/:id` - Update a todo
- `DELETE /api/todos/:id` - Delete a todo

### Expected Todo Object Structure

```javascript
{
  id: "unique-id",
  title: "Todo title",
  description: "Optional description",
  priority: "low" | "medium" | "high",
  dueDate: "2024-01-01", // ISO date string
  completed: false,
  createdAt: "2024-01-01T00:00:00.000Z"
}
```

## Technologies Used

- **React 19** - Frontend framework
- **Bootstrap 5** - CSS framework
- **Axios** - HTTP client
- **Font Awesome** - Icons
- **Vite** - Build tool