



# 🔐 Authify – Spring Security with JWT

Authify is a Spring Boot application that demonstrates JWT-based authentication and role-based authorization using Spring Security.  
The application follows a stateless security architecture suitable for RESTful backend services.

---

## 🧠 Security Architecture Overview

This project uses JWT-only authentication:
- No HTTP sessions
- No Basic Authentication
- Fully stateless request handling

Security is implemented in three clear layers:

### 1. Authentication
- User logs in using email and password
- Passwords are securely stored using BCrypt hashing

### 2. JWT Validation
- A signed JWT token is generated after successful login
- A custom JWT filter validates the token on every request
- SecurityContext is populated using token claims

### 3. Authorization
- Access control is enforced using roles (USER, ADMIN)
- Rules are centralized using Spring Security configuration and annotations

---

## 🔑 Authentication Flow

1. User registers using the public register API  
2. User logs in using email and password  
3. Credentials are authenticated using AuthenticationManager  
4. A JWT token is generated and returned  
5. Client sends the JWT in Authorization: Bearer <token> header  
6. JWT filter validates the token and grants access  

Credentials are used only once during login.  
All subsequent requests rely on JWT authentication.

---

## 👥 Roles & Access Control

- ROLE_USER – Default role for all registered users  
- ROLE_ADMIN – System-controlled role  

Admin users are not created via public APIs.  
They are bootstrapped internally to prevent privilege escalation.

---

## 🛡️ Authorization Strategy

- Public APIs → permitAll()  
- Protected APIs → authentication required  
- Admin-only APIs → role-based access control  

Authorization is enforced using URL-based security rules and method-level security.

```java
@EnableMethodSecurity
@PreAuthorize("hasRole('ADMIN')")
````

---

## 🔌 API Endpoints

### 🔓 Public Endpoints

Register User
POST /api/v1/profile/register

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

Login (Generate JWT)
POST /api/v1/auth/login

```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

### 🔐 Protected Endpoints

Welcome (Any Authenticated User)
GET /api/v1/profile/welcome

Authorization: Bearer <JWT_TOKEN>

---

### 🔒 Admin-Only Endpoints

Get All Profiles
GET /api/v1/profile/all

* ROLE_ADMIN → Allowed
* ROLE_USER → 403 Forbidden

Delete Profile
DELETE /api/v1/profile/delete/{email}

Authorization: Bearer <JWT_TOKEN>

---

## ⚙️ Key Security Components

* SecurityFilterChain – Defines public and protected APIs
* CustomUserDetailsService – Loads user details from database
* DaoAuthenticationProvider – Handles authentication logic
* BCryptPasswordEncoder – Secure password hashing
* JwtAuthenticationFilter – Validates JWT on every request
* JwtUtil – Token generation and validation
* EnableMethodSecurity – Enables method-level authorization

---

## ✅ Summary

This project demonstrates a clean, secure, and production-ready implementation of Spring Security using JWT, following modern best practices and avoiding common security mistakes.


