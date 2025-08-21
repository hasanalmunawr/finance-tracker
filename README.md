# Finance Tracker Service

This repository contains a Spring Boot application for managing personal finances.  
The application helps users record expenses, allocate budgets from salary, track savings, and even manage debts and obligations.  
It is designed as a simple yet practical tool to improve financial planning and provide better visibility of income and spending.
## Getting Started

### Table of Contents
> - [API Documentation](#api-documentation)
> - [Prerequisites](#prerequisites)
> - [Project Structure](#project-structure)
> - [Installation](#installation)
> - [API Usage](#api-usage)
> - [Author](#author)
> - [License](#license)

---

## API Documentation

Backend: https://fintrack-api.hasanalmunawar.my.id/api/swagger-ui

Actuator: https://fintrack-api.hasanalmunawar.my.id/api/actuator/info

---

### Prerequisites
- Java SDK 17 or above
- Apache Maven 3.4.1 or above
- MariaDb 
- Jasper Report
- Gmail Service

---



## Project Structure

```
📁 github/                     
└── 📁 workflows/               
│    └── 📄 main.yaml            # CI/CD pipeline (e.g., build, test, deploy)
│
📁 docs/                     
└──📄 FINANCE-TRACKER.postman_collection.json            # Postman collection API
│
📁 nginx/                     
└──📄 fintrack-api.conf           # Proxxy setup
│    
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
│
📄 Dockerfile                                        # Build image Spring Boot untuk Docker
📄 docker-compose.yml                               # Running container database + Spring Boot
│
📄 pom.xml                                           # Dependency dan config Maven

```


--- 
## Installation Guide

### API Usage
- [Postman Collection](docs/FINANCE-TRACKER.postman_collection.json)

### 


Follow the steps below to install and run the System locally.

---

### Installation

1. Clone the repo `git clone https://github.com/hasanalmunawr/finance-tracker.git`
2. Install Maven Dependencies `mvn clean install`
3. Configure properties in `application-dev.yml` (for development) file
    ```yml
    spring:
      datasource:
          url: jdbc:mariadb://<your-database-host>/financial_tracker # The URL of your MariaDb database
          username: <your-username> # The username of your MariaDb database
          password: <your-password> # The password of your MariaDb database
      jpa:
          hibernate:
            naming:
                implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
                physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
            ddl-auto: update # (Optional) This option automatically updates the database schema to match your entities.
          properties:
            hibernate:
                dialect: org.hibernate.dialect.MariaDBDialect # This option specifies the SQL dialect of your database
                format_sql: true # (Optional) This option pretty-prints SQL statements in the console

      mail:
        host: ${MAIL_HOST}
        port:  ${MAIL_PORT}
        username: ${MAIL_USERNAME}
        password: ${MAIL_PASSWORD}
        properties:
          mail:
            smtp:
              auth: true
              ssl:
                enable: true
              socketFactory:
                port: 465
                class: javax.net.ssl.SSLSocketFactory
                fallback: false
          transport:
            protocol: smtp
        default-encoding: UTF-8
    ```
4. Run test `mvn test`
5. Run the application `mvn spring-boot:run`

---


### Author
- [Hasan Almunawar](https://www.linkedin.com/in/hasan-almunawar/)
- [Email](mailto:hasanalmunawar09@gmail.com)

---

### License
Distributed under the MIT License. See [LICENSE](LICENSE) for more information.





