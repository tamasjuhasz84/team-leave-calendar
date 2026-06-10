# Team Leave Calendar

A simple team leave management application built as part of a technical assessment.

The application allows team members' leave requests to be managed and provides visibility into an on-call rotation schedule with conflict detection.

---

## Features

### Team Members

- View team members
- Team members are stored in PostgreSQL
- Automatic sample data initialization

### Leave Requests

- Create leave requests
- Prevent overlapping leave requests for the same person
- View all leave requests
- Filter leave requests by:
  - Team member
  - Status
- Add optional comments

### Leave Approval Workflow

Leave requests can have one of the following statuses:

- PENDING
- APPROVED
- REJECTED

Requests can be approved or rejected directly from the UI.

### On-Call Rotation

- Weekly on-call schedule
- Rotates through team members
- Conflict detection for approved leave requests
- Suggested replacement employee when available

### Calendar-style View

- Alternative visual representation of leave requests
- Status-based highlighting

---

## Technology Stack

### Backend

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

### Frontend

- Vue 3
- Vite
- Axios
- SCSS

### Infrastructure

- Docker
- Docker Compose

---

## Running the Application

### Start PostgreSQL

```bash
docker compose up -d
```

### Start Backend

```bash
cd backend

./mvnw spring-boot:run
```

Or run the application directly from IntelliJ IDEA.

Backend URL:

```
http://localhost:8080
```

### Start Frontend

```bash
cd frontend

npm install
npm run dev
```

Frontend URL:

```
http://localhost:5173
```

---

## API Documentation

### Team Members

#### Get all team members

```http
GET /api/team-members
```

---

### Leave Requests

#### Get all leave requests

```http
GET /api/leave-requests
```

#### Filter leave requests

```http
GET /api/leave-requests?teamMemberId=1

GET /api/leave-requests?status=APPROVED

GET /api/leave-requests?teamMemberId=1&status=APPROVED
```

#### Create leave request

```http
POST /api/leave-requests
```

Example request:

```json
{
  "teamMemberId": 1,
  "startDate": "2026-08-10",
  "endDate": "2026-08-15",
  "reason": "Summer holiday",
  "comments": "Vacation with family"
}
```

#### Update leave request status

```http
PATCH /api/leave-requests/{id}/status
```

Example request:

```json
{
  "status": "APPROVED"
}
```

---

### On-call Schedule

#### Get on-call schedule

```http
GET /api/on-call?weeks=8
```

---

## Validation Rules

- Leave requests cannot overlap for the same team member
- Start date must be before or equal to end date
- Team member must exist
- Status transitions are validated on the backend

---

## Automated Tests

The backend includes unit tests covering:

- Leave request creation
- Overlap detection
- Status transitions
- On-call schedule generation

Run tests:

```bash
cd backend

./mvnw test
```

---

## Assumptions

- Authentication is not required
- Team members are managed by the system
- On-call rotation is based on team member order
- Replacement suggestions are informational only

---

## Optional Improvements Implemented

- Filtering by team member
- Filtering by status
- Leave approval workflow
- Comments on leave requests
- Automated backend tests
- Dockerized PostgreSQL setup
- REST API documentation
- Automatic replacement suggestion
- Enhanced conflict highlighting

---

## Features Not Implemented

The following optional or future improvements were not implemented:

- Full calendar month view
- Authentication and authorization
- Editable team member management
- Email notifications
- OpenAPI / Swagger integration

## Future Improvements

- Full calendar month view
- Authentication and authorization
- Editable team members
- Email notifications
- OpenAPI / Swagger integration
