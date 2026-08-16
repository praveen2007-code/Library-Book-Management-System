# Library Book Management System 📚

> **Java-based Library Management System** — A desktop application for managing books, users, borrowing and returning operations, with MySQL database integration and SMS reminders for due dates.

---

## 🚀 Features

| Module                | Description                                             |
| --------------------- | ------------------------------------------------------- |
| 📚 Book Management    | Add, search, and manage library books                   |
| 👤 User Management    | Manage library users and their accounts                 |
| 🔐 Login System       | Secure login and role-based access                      |
| 📖 Borrow Books       | Record book borrowing transactions                      |
| ↩️ Return Books       | Manage returned books and update availability           |
| 🔍 Search Books       | Search for books in the library                         |
| 📱 SMS Reminder       | Send SMS reminders for books approaching their due date |
| 🗄️ MySQL Database    | Store books, users, and transaction information         |
| 🖥️ Desktop Interface | User-friendly Java GUI                                  |

---

## 📁 Project Structure

```text
LibraryBookManagementSystem/
│
├── lib/
│   └── mysql-connector-j-9.6.0.jar
│
├── sql/
│   └── create_tables.sql
│
├── src/
│   ├── AddBookPanel.java
│   ├── AdminDashboard.java
│   ├── BorrowBookPanel.java
│   ├── DBConnection.java
│   ├── LoginFrame.java
│   ├── Main.java
│   ├── ReturnBookPanel.java
│   ├── SearchBookPanel.java
│   ├── SMSSender.java
│   ├── UIUtils.java
│   └── UserDashboard.java
│
├── .gitignore
└── README.md
```

---

## 🛠️ Tech Stack

| Layer                 | Technology         |
| --------------------- | ------------------ |
| Programming Language  | Java               |
| GUI                   | Java Swing         |
| Database              | MySQL              |
| Database Connectivity | JDBC               |
| SMS Service           | Twilio             |
| IDE                   | Visual Studio Code |
| Version Control       | Git & GitHub       |

---

## ⚙️ Requirements

Before running the project, install:

* Java JDK 17 or later
* MySQL Server
* MySQL Workbench (recommended)
* Git
* Visual Studio Code or another Java IDE

---

## 🗄️ Database Setup

### 1. Create the database

Open MySQL and create a database for the application.

```sql
CREATE DATABASE library_management;
```

### 2. Create the tables

Open:

```text
sql/create_tables.sql
```

Run the SQL script in MySQL Workbench.

### 3. Configure the database connection

Update the database credentials in:

```text
src/DBConnection.java
```

Set your own:

* MySQL username
* MySQL password
* Database name

**Do not upload database passwords or other credentials to GitHub.**

---

## ▶️ How to Run

### 1. Clone the repository

```bash
git clone https://github.com/praveen2007-code/Library-Book-Management-System.git
```

### 2. Open the project

Open the project folder in Visual Studio Code.

### 3. Configure MySQL

Make sure MySQL Server is running and the database/tables have been created.

### 4. Configure the MySQL Connector

The project uses MySQL Connector/J for JDBC database connectivity.

The connector is located in:

```text
lib/mysql-connector-j-9.6.0.jar
```

Make sure the JAR is included in the Java class path when running the project.

### 5. Run the application

Run:

```text
src/Main.java
```

---

## 📱 SMS Reminder System

The application includes an SMS reminder feature that can notify users about upcoming book due dates.

The SMS functionality is implemented in:

```text
src/SMSSender.java
```

The project uses the **Twilio API** for sending SMS messages.

⚠️ **Security:** Never store Twilio Account SID, Auth Token, API keys, passwords, or other secrets directly in source code or commit them to GitHub.

---

## 🔄 Application Workflow

```text
        Start Application
               ↓
          Login Screen
               ↓
      ┌────────┴────────┐
      ↓                 ↓
   Admin             User
      ↓                 ↓
Manage Books       Search Books
      ↓                 ↓
Add / Search       Borrow Book
      ↓                 ↓
  Manage Users     Return Book
                        ↓
                  Due Date Reminder
                        ↓
                    SMS Alert
```

---

## 📚 Main Modules

### Admin Dashboard

Provides administrators with options to manage books and library operations.

### User Dashboard

Allows users to search for books, borrow books, and return borrowed books.

### Book Management

Administrators can add and manage books available in the library.

### Borrow & Return

The system records borrowing and returning transactions and updates book availability.

### Search

Users can search for available books in the library.

### SMS Notifications

Users can receive reminders about upcoming book due dates.

---

## 🎯 Project Objective

The objective of this project is to develop a computerized library management system that simplifies book management, user management, borrowing and returning operations, and due-date notifications.

The system reduces manual work and provides a centralized platform for managing library activities.

---

## 🔮 Future Enhancements

* 📊 Advanced admin dashboard and analytics
* 📧 Email notifications
* 📱 Mobile application
* 🌐 Web-based version
* 📈 Book usage reports
* 🔔 Automated overdue notifications
* ☁️ Cloud database integration
* 👥 Multiple library support
* 🔐 Improved authentication and security

---

## 👨‍💻 Author

**Praveen**

GitHub: https://github.com/praveen2007-code

---

## 📄 License

This project was developed for educational and academic purposes.
