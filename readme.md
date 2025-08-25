# Product API

## 📌 Project Description
This project is a **challenge**: a backend service developed in **Java 17 with Spring Boot**, providing a full CRUD for managing products.  
It includes:
- Authentication with **JWT**
- Database integration with **MySQL**
- API documentation using **Swagger/OpenAPI**

---

## 🚀 Running Locally (Development)

In development mode, the project can be run using **Docker Compose**.

### 1. Clone the repository
git clone https://github.com/your-username/product-api.git
cd product-api

### 2. Configure environment variables
You can use the provided script:
source setenv.sh

Or manually:
- export DB_URL="jdbc:mysql://localhost:3306/productdb"
- export DB_USERNAME="user"
- export DB_PASSWORD="strongPassword"
- export JWT_SECRET="bXlTM2N1cjNTdXAzckwwbmdKV1RLM3khMjAyNSQlXiYqKCk="
- export SPRING_PROFILES_ACTIVE="dev"

### 3. Run Docker Compose
docker-compose up --build -d

The API will be available at:
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html

---

##  Running in Production (AWS)

In production, the infrastructure is provisioned using **Terraform** and deployed through the **AWS CLI**.  
There are **two main deployment strategies**:

## Option 1: Deploy with ECS + Docker

1. Build the Docker image:
   docker build -t product-api .

2. Push the image to Amazon ECR:

   a. Create the ECR repository:
      aws ecr create-repository --repository-name product-api

   b. Authenticate Docker with ECR:
      aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account_id>.dkr.ecr.us-east-1.amazonaws.com

   c. Tag the image:
      docker tag product-api:latest <account_id>.dkr.ecr.us-east-1.amazonaws.com/product-api:latest

   d. Push the image:
      docker push <account_id>.dkr.ecr.us-east-1.amazonaws.com/product-api:latest


4. Deploy with Terraform
    - Terraform will create:
        - ECS Cluster
        - ECS Task Definition (pointing to the ECR image)
        - ECS Service
        - Load Balancer
        - RDS MySQL Database

5. Access
    - The service will be available through the Load Balancer endpoint created by Terraform.

---

## 🛠️ Technologies Used
- Java 17
- Spring Boot
- Spring Security (JWT Authentication)
- Spring Data JPA (MySQL)
- Docker & Docker Compose
- Swagger / OpenAPI
- JUnit & Mockito
- Terraform & AWS CLI

---

## 📄 API Documentation
After starting the project, API documentation can be accessed via Swagger at:

http://localhost:8080/swagger-ui.html




