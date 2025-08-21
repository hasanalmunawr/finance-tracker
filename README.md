# Product Customer Service

**Product Customer Service** is a web-based platform designed to manage after-sales service workflows. It helps teams efficiently handle service orders and customer complaints, while also maintaining relevant master data such as products, regions, and branches.

This application provides a centralized dashboard and structured role-based access to streamline post-sale service operations.

---

## Tech Stack

This project leverages a modern and robust technology stack to ensure scalability, maintainability, and performance:

- **Spring Boot** – Powers the backend with a fast and reliable RESTful API framework.
- **Docker** – Ensures consistent development and deployment environments using containerization.
- **PostgreSQL** – A powerful, open-source relational database system for secure and efficient data management.


## Features

-  **Dashboard Analytics** – Overview of key service metrics
-  **Master Data Management** – Products, colors, regions, branches, etc.
-  **Service Order Management** – Track and process customer service requests
-  **Menu Access Control** – Customizable navigation based on roles
-  **Role & Permission Management** – Define and assign access authority
-  **User Management** – Create, edit, and manage system users

---

## Project Structure

```
📁 github/                     
└── 📁 workflows/               
│    └── 📄 main.yaml            # CI/CD pipeline (e.g., build, test, deploy)
│
📁 src/
└── 📁 main/
│   ├── 📁 java/
│   │   └── 📁 hasanalmunawr/dev/backend_spring/
│   │       ├── 📁 base/                            # Usual Module & reusable (shared kernel)
│   │       │   ├── 📁 api/                         # Base response/request DTO (e.g., ApiResponse<T>)
│   │       │   ├── 📁 config/                      # Global configurations (e.g., WebConfig, CORS)
│   │       │   ├── 📁 constant/                    # Static constants, enums, etc.
│   │       │   ├── 📁 exception/                   # Custom exceptions & global handler
│   │       │   ├── 📁 helper/                      # Utility classes (e.g., DateUtil, TokenUtil)
│   │       │   ├── 📁 model/                       # Base model (e.g., BaseEntity, AuditEntity)
│   │       │   ├── 📁 repository/                  # Generic repository & base interfaces
│   │       │   ├── 📁 task/                        # Processor task (e.g., scheduled jobs, queue consumer)
│   │       │
│   │       ├── 📁 user/                            # Specific Module (domain: user management)
│   │       │   ├── 📁 api/                         # DTO for User Module
│   │       │   │   ├── 📁 request/                 # Request DTO (e.g., CreateUserRequest)
│   │       │   │   └── 📁 response/                # Response DTO (e.g., UserDetailResponse)
│   │       │   ├── 📁 controller/                  # REST Controllers
│   │       │   ├── 📁 model/                       # User's Entity
│   │       │   ├── 📁 repository/                  # User's Repository (extends JpaRepository)
│   │       │   └── 📁 service/                     # Interface service layer
│   │       │       └── 📁 impl/                    # Implementasi logic business
│   │       │
│   │       ├── 📁 web/                             # Modul Web MVC 
│   │       │   ├── 📁 config/                      # Config untuk web (e.g., Security, Interceptor)
│   │       │   ├── 📁 model/                       # ViewModel / form object
│   │       │   └── 📁 service/                     # Web Module Service
│   │       │
│   │       └── 📄 BackendApplication.java          # Main class (entry point aplikasi Spring Boot)
│   │
│   └── 📁 resources/
│       ├── 📄 application.yml                      # Main Config
│       ├── 📄 application-dev.yml                  # Dev Config
│       └── 📄 application-prod.yml                 # Prod Config
│      
│    
│
📁 sql/
└── 📄 after_sales.sql                              # Skema & dummy data SQL
│
📄 Dockerfile                                        # Build image Spring Boot untuk Docker
📄 docker-compose.yml                               # Running container database + Spring Boot
│
📄 pom.xml                                           # Dependency dan config Maven

```


--- 
## Installation Guide

Follow the steps below to install and run the System locally.

---

### 1. Clone the Repository

First, clone the project from GitLab:

```bash
  git clone https://github.com/hasanalmunawr/product-customer-service.git
  cd product-customer-service
```

### 2.  Install Dependencies
Install all necessary backend :
```bash
  mvn install           
```


### Default Admin Login
If you import the SQL file, you can log in using the following credentials:

- Email: `adminsuper@gmail.com`
- Password: `12345678`

--- 
## Developer
**Hasan Almunawar**  
📧 Email: [hasanalmunawar9@gmail.com](mailto:hasanalmunawar9@gmail.com)  
📅 Year: 2025




