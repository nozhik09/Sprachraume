# Sprachraume (SprachSpace)
Backend platform for organizing online language practice rooms where users can create, join, and manage real-time discussions.

The system provides user management, room workflows, invitations, and integration with video communication via Daily.co.

---

## 🚀 Features

### Authentication and Security

* User registration and login
* JWT-based authentication (access & refresh tokens)
* Token refresh flow
* Google OAuth authentication
* Admin role management
* User blocking and unblocking

### User Management

* Update user profile data
* Search users
* Retrieve users by ID
* User rating system
* Upload and retrieve avatars
* Delete users

### Room Management

* Create discussion rooms
* View all rooms and active rooms
* Get room details by ID
* Filter rooms by language, status, age, and category
* Extend room duration
* Manage online room sessions

### Invitations and Participation

* Send room join requests
* Accept or decline invitations
* Admin approval/decline of join requests
* Invite users directly to a room
* Track pending, accepted, and received invitations

### Language Features

* Add and remove native languages
* Add and remove learning languages
* Search users by language preferences
* Match users by native and learning language combinations

### Notifications

* Get user notifications
* Track notification status
* Mark notifications as read

### Additional Features

* Category management
* Health-check endpoint
* Swagger/OpenAPI documentation

---

## 🎥 Video Communication (Daily.co Integration)

The project is integrated with **Daily.co API** to support real-time video/voice communication.

* Room session creation via Daily API
* External video infrastructure integration
* Scalable video communication support

---

## 🛠 Tech Stack

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA (Hibernate)
* PostgreSQL
* Liquibase
* Maven
* Lombok
* Swagger (OpenAPI)
* Daily.co API
* Railway (deployment)

---

## 🧠 Architecture

* Layered architecture (Controller → Service → Repository)
* DTO pattern (avoids entity exposure and serialization issues)
* RESTful API design
* External API integration (Daily.co)

---

## ⚙️ Requirements

Before running the project, make sure you have installed:

* Java 17+
* Maven 3.8+
* PostgreSQL
* Git

---

## 🔑 Environment Variables

The application requires the following environment variables:

| Variable           | Description                 |
| ------------------ | --------------------------- |
| DATABASE_URL       | PostgreSQL database URL     |
| PGUSER             | PostgreSQL username         |
| PGPASSWORD         | PostgreSQL password         |
| PORT               | Server port (default: 8080) |
| KEY_ACCESS         | JWT access token secret     |
| KEY_REFRESH        | JWT refresh token secret    |
| YOUR_DAILY_API_KEY | API key for Daily.co        |

---

### Example

```env
DATABASE_URL=jdbc:postgresql://localhost:5432/sprachraume
PGUSER=postgres
PGPASSWORD=postgres
PORT=8080
KEY_ACCESS=your_access_secret
KEY_REFRESH=your_refresh_secret
YOUR_DAILY_API_KEY=your_daily_api_key
```

---

## ⚙️ How to Run Locally

### 1. Clone the repository

```bash
git clone https://github.com/nozhik09/Sprachraume
cd sprachraume
```

### 2. Create PostgreSQL database

```sql
CREATE DATABASE sprachraume;
```

### 3. Set environment variables

Configure all required environment variables (see above).

### 4. Build the project

```bash
mvn clean package
```

### 5. Run the application

```bash
mvn spring-boot:run
```

Or run the generated jar:

```bash
java -jar target/sprachraume-0.0.1-SNAPSHOT.jar
```

---

## 🌐 API Links

### Production (Railway)

* Base URL: https://sprachraume-production.up.railway.app/api
* Swagger UI: https://sprachraume-production.up.railway.app/api/swagger-ui/index.html
* Health Check: https://sprachraume-production.up.railway.app/api/health

### Local

* Base URL: http://localhost:8080/api
* Swagger UI: http://localhost:8080/api/swagger-ui/index.html
* Health Check: http://localhost:8080/api/health

---

## 🗄 Database

* PostgreSQL is used as the main database
* Schema is managed via **Liquibase**
* Migrations are located in:

```
classpath:db/changelog/changelog-master.xml
```

---

## 📂 File Storage

* Avatar upload is supported
* Files are stored on the server (configurable via environment)

---

## 📌 Notes

* The project is backend-focused and can be tested via Swagger UI
* Daily.co integration requires a valid API key
* PostgreSQL must be running before application start
* Environment variables are required for correct startup

---

## 👨‍💻 Author

Vladyslav Nozhenko
Junior Java Developer

* LinkedIn: https://www.linkedin.com/in/vladyslav-nozhenko-165124274/
* GitHub: https://github.com/nozhik09
