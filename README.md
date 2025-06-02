---
created_date: 20/05/2025
updated_date: 02/06/2025
---
# Calendar Login System

## 🎯 Project Overview

This is a JavaFX-based calendar application with user authentication and event management features. Users can log in, view their monthly calendar, add/edit/delete events, and all data is stored securely in a Microsoft SQL Server database using Hibernate ORM.

---

## 🧩 Features

- 🔐 User login system
- 🗓️ Monthly calendar view with day highlighting
- ✏️ Add, edit, or delete calendar events
- 💾 Events are saved to a Microsoft SQL Server database
- 📅 Highlight days with events (yellow for pending, green for done)

---

## ⚙️ Technologies Used

|              |                      |
| ------------ | -------------------- |
| Language     | Java SDK 24          |
| UI Framework | JavaFX               |
| ORM          | Hibernate 6          |
| Database     | Microsoft SQL Server |
| Build Tool   | Maven                |
| IDE          | IntelliJ IDEA        |

---

## 🛠️ Requirements

Before running the application, make sure you have:

- ✅ Java 24 installed
- ✅ Microsoft SQL Server set up and running
- ✅ JavaFX SDK (if not using JDK with JavaFX built-in)
- ✅ Hibernate and JDBC drivers configured

---

## Database Setup

### Database Tables:

```sql
CREATE TABLE Sexes (

SexID INT IDENTITY(1,1) PRIMARY KEY,

SexName NVARCHAR(10) NOT NULL UNIQUE

);


CREATE TABLE Roles (

RoleID INT IDENTITY(1,1) PRIMARY KEY,

RoleName NVARCHAR(50) NOT NULL UNIQUE

);
```

```sql
CREATE TABLE Users (

UserID INT IDENTITY(1,1) PRIMARY KEY,

Email NVARCHAR(255) NOT NULL UNIQUE,

Password NVARCHAR(255) NOT NULL,

Username NVARCHAR(255) NOT NULL UNIQUE,

Birthday DATE,

RoleID INT NOT NULL,

FirstName NVARCHAR(255),

LastName NVARCHAR(255),

Phone NVARCHAR(20),

SexID INT NOT NULL

);
```

``` sql
CREATE TABLE calendarEvent (

EventID INT IDENTITY(1,1) PRIMARY KEY,

Name NVARCHAR(255) NOT NULL,

Description NVARCHAR(MAX),

UserID INT NOT NULL FOREIGN KEY REFERENCES Users(UserID),

EventDate DATE NOT NULL,

Done BIT DEFAULT 0 NOT NULL

);
```

Make sure to update `hibernate.cfg.xml` with this DB connection string:

``` xml
<?xml version='1.0' encoding='UTF-8'?>  
<!DOCTYPE hibernate-configuration PUBLIC  
        "-//Hibernate/Hibernate Configuration DTD 5.3//EN"  
        "http://hibernate.org/dtd/hibernate-configuration-3.0.dtd">  
<hibernate-configuration>  
    <session-factory>  
        <!-- Database connection settings -->  
        <property name="connection.driver_class">com.microsoft.sqlserver.jdbc.SQLServerDriver</property>  
        <property name="connection.url">jdbc:sqlserver://46.13.167.200:20500;databaseName=PablovaDB</property>  
        <property name="connection.username">Pablo</property>  
        <property name="connection.password">Pivo123!</property>  
        <property name="dialect">org.hibernate.dialect.SQLServer2012Dialect</property>  
        <property name="show_sql">true</property>  
        <!-- Register entity classes -->  
        <mapping class="org.example.loginsystem.User"/>  
        <mapping class="org.example.loginsystem.CalendarEvent"/>  
  
    </session-factory>
</hibernate-configuration>
```
by default you can use my database which is

---

## How to Run the App

### Step-by-step instructions:

1. **Clone the repository** :

	git clone https://github.com/paronim5/loginSystemJavaFX.git

- **Build the project**
	Build directly in your IDE

- **Run the application**

	- Use your IDE or execute:

1. **Sing up**
	- Sing up with your email, password and other required informations
	- You'll be taken to the login screen

2. **Login**

	- Enter valid email, username, and password
	- You'll be taken to the calendar screen
3. **Use the Calendar**

	- Click on any day to add or edit an event
	- Use the menu bar to navigate months or add new events

---
