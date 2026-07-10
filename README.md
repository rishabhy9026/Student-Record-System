# 🎓 Student Record Management System

A modern and interactive **Student Record Management System** developed using **Java and Java Swing**. The project provides an easy-to-use graphical interface for managing student records, analyzing academic performance, and demonstrating practical implementations of core **Data Structures and Algorithms (DSA)** concepts.

The system combines student data management, CGPA analytics, Stack and Queue operations, searching, sorting, and graphical visualization in a professional desktop application.

---

## 📌 Project Overview

Managing student records manually can become difficult when the number of students increases. The **Student Record Management System** provides a simple and interactive solution to store, search, organize, and analyze student information.

The application allows users to add student records, view all students, search using registration numbers, sort students according to CGPA, identify top-performing students, manage exam registrations, and visualize CGPA performance using graphs.

The project also demonstrates the practical use of **Arrays, Stack, Queue, Searching, Sorting, and Binary Search Tree concepts**.

---

## ✨ Features

### 👨‍🎓 Student Management

* Add new student records
* Store student name, roll number, registration number, course, and CGPA
* View all student records
* Search students using registration number
* Remove student records
* Validate student input
* Support multiple academic courses

### 📊 Academic Analytics

* Sort students by CGPA from highest to lowest
* Display the Top 3 performing students
* Calculate average CGPA
* Display highest CGPA
* Display lowest CGPA
* Show total number of students
* Interactive statistics dashboard

### 📚 Data Structures Implementation

* **Array** for storing student records
* **Stack** for tracking recently added students
* **Queue** for managing exam registrations
* **Binary Search Tree (BST)** implementation for student records
* Searching and sorting operations

### 📝 Exam Registration System

* Register students for examinations
* Maintain students in an exam queue
* Display the current exam queue
* Queue-based exam registration management

### 📈 Data Visualization

* CGPA distribution bar graph
* Visual academic performance analysis
* Dynamic graphical representation of student CGPA
* Interactive analytics dashboard

### 💾 Data Export

* Generate a formatted student database backup
* Display student information in an export-ready format
* Includes export date and complete student details

### 🎨 Modern User Interface

* Java Swing based graphical interface
* Dark theme dashboard
* Gradient header design
* Interactive category sidebar
* Hover-enabled buttons
* Professional statistic cards
* Color-coded management categories

---

## 🛠️ Technologies Used

| Technology         | Purpose                      |
| ------------------ | ---------------------------- |
| Java               | Core application development |
| Java Swing         | Graphical User Interface     |
| Java AWT           | Graphics and UI components   |
| Arrays             | Student data storage         |
| Stack              | Recent student tracking      |
| Queue              | Exam registration management |
| Binary Search Tree | Student record operations    |
| Graphics2D         | Graph and UI visualization   |

---

## 🧠 Data Structures Used

### 1. Array

An array is used to store student records in the system.

```java
static Student[] students = new Student[1000];
```

The system can maintain up to **1000 student records**.

### 2. Stack

A Stack is used to track recently added students.

```java
static Student[] recentStack = new Student[500];
static int top = -1;
```

The Stack follows the **LIFO (Last In, First Out)** principle.

### 3. Queue

A Queue is used for examination registration.

```java
static Student[] examQueue = new Student[500];
static int front = 0, rear = 0;
```

The Queue follows the **FIFO (First In, First Out)** principle.

### 4. Binary Search Tree

The project also includes a Binary Search Tree implementation for student records.

BST operations include:

* Insert student
* Search student by roll number
* Delete student
* Inorder traversal
* Display records in sorted order

---

## 📂 Project Structure

```text
Student-Record-System
│
├── .vscode
│   ├── launch.json
│   └── settings.json
│
└── student-record-system
    │
    └── java project
        ├── Student.java
        ├── StudentBST.java
        └── StudentRecordSystem.java
```

### File Description

| File                       | Description                                        |
| -------------------------- | -------------------------------------------------- |
| `Student.java`             | Defines the Student object and student properties  |
| `StudentBST.java`          | Implements Binary Search Tree operations           |
| `StudentRecordSystem.java` | Main GUI application and student management system |

---

## 🚀 How to Run the Project

### Prerequisites

Make sure the following software is installed:

* Java Development Kit (JDK)
* Java 8 or above
* Visual Studio Code, IntelliJ IDEA, Eclipse, or any Java-supported IDE

### Step 1: Clone the Repository

```bash
git clone https://github.com/rishabhy9026/Student-Record-System.git
```

### Step 2: Open the Project Folder

```bash
cd Student-Record-System
```

### Step 3: Navigate to the Java Project Folder

```bash
cd "student-record-system/java project"
```

### Step 4: Compile the Java Program

```bash
javac StudentRecordSystem.java
```

### Step 5: Run the Application

```bash
java StudentRecordSystem
```

The **Student Record Management System dashboard** will open.

---

## 💻 Application Modules

The dashboard contains the following modules:

| Module        | Function                              |
| ------------- | ------------------------------------- |
| ➕ Add         | Add a new student                     |
| 📋 View All   | Display all student records           |
| 🔍 Find       | Search student by registration number |
| 📊 CGPA Sort  | Sort students by CGPA                 |
| 🏆 Top 3      | Display top three students            |
| 🗑️ Remove    | Delete a student record               |
| 🕒 Stack      | Show recently added students          |
| 📝 Register   | Register student for exam             |
| 📚 Exam Queue | Display examination queue             |
| 📈 Chart      | Display CGPA bar graph                |
| 📊 Board      | Open statistics dashboard             |
| 💾 Save       | Generate student database backup      |

---

## 📊 Student Information Stored

The system maintains the following information for every student:

* Student Name
* Roll Number
* Registration Number
* Course
* CGPA

Supported courses include:

* B.Tech
* M.Tech
* BCA
* MCA
* BBA
* MBA
* B.Sc
* M.Sc

---

## 🎯 Learning Objectives

This project was developed to understand and implement:

* Object-Oriented Programming in Java
* Java Swing GUI development
* Event-driven programming
* Arrays and record management
* Stack implementation
* Queue implementation
* Binary Search Tree operations
* Searching techniques
* Sorting algorithms
* Data visualization using Java Graphics
* Student data analytics

---

## 🔮 Future Enhancements

The project can be further improved by adding:

* MySQL database integration
* User login and authentication
* Admin and student dashboards
* Student profile images
* Attendance management
* Marks and result management
* PDF report generation
* CSV and Excel export
* Cloud database integration
* Advanced search filters
* Automatic data backup
* REST API integration

---

## 🤝 Contribution

Contributions, suggestions, and improvements are welcome.

To contribute:

1. Fork the repository
2. Create a new branch
3. Make your changes
4. Commit your changes
5. Push the branch
6. Create a Pull Request

---

## 👨‍💻 Author

**Rishabh Yadav**

B.Tech Computer Science and Engineering Student

GitHub: `rishabhy9026`

---

## ⭐ Support

If you found this project useful or interesting, consider giving the repository a **Star ⭐**.

Your support motivates me to build and share more projects.

---

### 🎓 Student Record Management System

**Manage Students | Analyze Performance | Implement Data Structures**
