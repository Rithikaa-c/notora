# 📝 Notora

## 📌 About the Project

**Notora** is a web-based note management and sharing platform that allows users to create, manage, and share notes in a secure and organized way.

The application provides user authentication and a personalized dashboard where users can manage their notes and interact with shared notes.

---

## ✨ Features

- 🔐 User Registration and Login
- 📧 Forgot Password functionality
- 🔑 OTP-based password reset
- 📝 Create and manage personal notes
- 🤝 Share notes with other users
- 📂 View shared notes
- 📊 Personalized user dashboard
- 👤 User account management
- 🔒 Secure user authentication

---

## 🛠️ Technologies Used

### Backend
- Java
- Spring Boot
- Spring Data JPA
- Maven

### Frontend
- HTML
- CSS
- JavaScript

### Database
- MySQL

---

## 📂 Project Structure

```text
notora/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/rithikaa/notora/
│   │   │       ├── controller/
│   │   │       │   ├── AdminLoginController
│   │   │       │   ├── DashboardController
│   │   │       │   ├── ForgotPasswordController
│   │   │       │   ├── HomeController
│   │   │       │   ├── LoginController
│   │   │       │   ├── NotesController
│   │   │       │   ├── RegisterController
│   │   │       │   └── SharedNotesController
│   │   │       │
│   │   │       ├── model/
│   │   │       │   ├── Admin
│   │   │       │   ├── Note
│   │   │       │   └── User
│   │   │       │
│   │   │       ├── repository/
│   │   │       │   ├── AdminRepository
│   │   │       │   ├── NoteRepository
│   │   │       │   └── UserRepository
│   │   │       │
│   │   │       ├── service/
│   │   │       │   ├── AdminService
│   │   │       │   ├── EmailService
│   │   │       │   ├── OtpService
│   │   │       │   └── UserService
│   │   │       │
│   │   │       └── NotoraApplication.java
│   │   │
│   │   └── resources/
│
├── .gitignore
├── pom.xml
└── README.md
