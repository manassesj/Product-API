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

### Option 1: ECS + Docker (Recommended)
1. Build the Docker image
   docker build -t product-api .

2. Push image to Amazon ECR
   aws ecr create-repository --repository-name product-api
   aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account_id>.dkr.ecr.us-east-1.amazonaws.com
   docker tag product-api:latest <account_id>.dkr.ecr.us-east-1.amazonaws.com/product-api:latest
   docker push <account_id>.dkr.ecr.us-east-1.amazonaws.com/product-api:latest

3. Deploy with Terraform
    - Terraform will create:
        - ECS Cluster
        - ECS Task Definition (pointing to the ECR image)
        - ECS Service
        - Load Balancer
        - RDS MySQL Database

4. Access
    - The service will be available through the Load Balancer endpoint created by Terraform.

---

### Option 2: EC2 + JAR (Simpler, less scalable)
1. Package the application
   ./mvnw clean package -DskipTests

2. Terraform creates resources
    - EC2 instance
    - Security Groups
    - RDS MySQL

3. Upload JAR to EC2
   scp -i my-key.pem target/product-api.jar ec2-user@<ec2-public-ip>:/home/ec2-user/

4. Run application
   ssh -i my-key.pem ec2-user@<ec2-public-ip>
   java -jar product-api.jar

Terraform can also provision EC2 with a **User Data Script** to run the JAR automatically at startup.

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

