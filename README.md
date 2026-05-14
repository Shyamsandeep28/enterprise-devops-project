# Enterprise DevOps CI/CD Project

## Project Overview

This project demonstrates a complete **Production-Grade DevOps CI/CD Pipeline** using:

- Java Spring Boot Microservices
- Maven
- Jenkins
- Docker
- Trivy
- SonarQube
- GitHub

The project contains two Spring Boot microservices:

1. `loginservice`
2. `catalogservice`

---

# Complete Architecture

```text
Developer
    ↓
GitHub
    ↓
Jenkins Pipeline
    ↓
Maven Build
    ↓
Trivy File System Scan
    ↓
SonarQube Analysis
    ↓
Docker Image Build
    ↓
Trivy Image Scan
    ↓
Docker Deployment
```

---

# Technology Stack

| Tool | Purpose |
|---|---|
| Java 17 | Application Runtime |
| Spring Boot | Microservices Framework |
| Maven | Build Tool |
| Jenkins | CI/CD Automation |
| Docker | Containerization |
| Trivy | Security Scanning |
| SonarQube | Code Quality Analysis |
| GitHub | Source Code Management |

---

# Project Structure

```text
enterprise-devops-project/

├── loginservice/
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src/
│   └── target/
│
├── catalogservice/
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src/
│   └── target/
│
└── Jenkinsfile
```

---

# AWS EC2 Setup

## EC2 Configuration

| Setting | Value |
|---|---|
| OS | Ubuntu 22.04 |
| Instance Type | t3.medium |
| Storage | 20 GB |

---

# Security Group Ports

| Port | Purpose |
|---|---|
| 22 | SSH |
| 8080 | Jenkins |
| 8081 | Login Service |
| 8082 | Catalog Service |
| 9000 | SonarQube |

---

# Installed Components on EC2

The following tools are installed on the same EC2 instance:

| Tool | Status |
|---|---|
| Jenkins | Installed |
| Docker | Installed |
| Maven | Installed |
| Git | Installed |
| Trivy | Installed |
| SonarQube | Running as Docker Container |

---

# Clone Repository

```bash
git clone https://github.com/Shyamsandeep28/enterprise-devops-project.git
```

---

# Maven Build

## Build Login Service

```bash
cd loginservice
mvn clean package
```

## Build Catalog Service

```bash
cd catalogservice
mvn clean package
```

---

# Generated JAR Files

## Login Service

```text
target/login-service-0.0.1-SNAPSHOT.jar
```

## Catalog Service

```text
target/catalogservice-0.0.1-SNAPSHOT.jar
```

---

# Dockerfile

## Login Service Dockerfile

```dockerfile
FROM eclipse-temurin:17

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]
```

---

## Catalog Service Dockerfile

```dockerfile
FROM eclipse-temurin:17

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","app.jar"]
```

---

# Build Docker Images

## Login Service

```bash
cd loginservice
docker build -t loginservice:v1 .
```

## Catalog Service

```bash
cd catalogservice
docker build -t catalogservice:v1 .
```

---

# Verify Docker Images

```bash
docker images
```

---

# Run Docker Containers

## Login Service Container

```bash
docker run -d -p 8081:8081 --name login-container loginservice:v1
```

## Catalog Service Container

```bash
docker run -d -p 8082:8082 --name catalog-container catalogservice:v1
```

---

# Verify Running Containers

```bash
docker ps
```

---

# Access Applications

## Login Service

```text
http://13.206.83.5:8081/login
```

## Catalog Service

```text
http://13.206.83.5:8082/products
```

---

# Jenkins Installation

## Install Java 21

```bash
sudo apt update
sudo apt install openjdk-21-jdk -y
```

---

## Install Jenkins

```bash
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | sudo tee \
  /usr/share/keyrings/jenkins-keyring.asc > /dev/null

echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
  https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null

sudo apt update

sudo apt install jenkins -y
```

---

## Start Jenkins

```bash
sudo systemctl start jenkins
sudo systemctl enable jenkins
```

---

## Check Jenkins Status

```bash
sudo systemctl status jenkins
```

---

## Access Jenkins

```text
http://13.206.83.5:8080
```

---

# Docker Permission for Jenkins

## Important

Jenkins requires Docker permission to build Docker images.

Run:

```bash
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
```

---

## Verify Jenkins Docker Access

```bash
groups jenkins
```

### Expected Output

```text
docker
```

---

# SonarQube Setup

## Run SonarQube Container

```bash
docker run -d \
--name sonarqube \
-p 9000:9000 \
sonarqube:lts-community
```

---

## Verify SonarQube Container

```bash
docker ps
```

---

## Access SonarQube

```text
http://13.206.83.5:9000
```

---

## SonarQube Default Credentials

```text
Username: admin
Password: admin
```

---

# Generate SonarQube Token

Navigate to:

```text
Administration
→ Security
→ Users
→ Tokens
```

Generate a token for Jenkins Pipeline usage.

---

# Sonar Scanner Installation

## Download Sonar Scanner

```bash
wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-7.0.2.4839-linux-x64.zip
```

---

## Install unzip

```bash
sudo apt install unzip -y
```

---

## Extract Scanner

```bash
unzip sonar-scanner-cli-7.0.2.4839-linux-x64.zip
```

---

## Move Scanner

```bash
sudo mv sonar-scanner-7.0.2.4839-linux-x64 /opt/sonar-scanner
```

---

## Verify Sonar Scanner

```bash
/opt/sonar-scanner/bin/sonar-scanner --version
```

---

# Trivy Installation

## Install Dependencies

```bash
sudo apt-get install wget apt-transport-https gnupg lsb-release -y
```

---

## Add Trivy Repository Key

```bash
wget -qO - https://aquasecurity.github.io/trivy-repo/deb/public.key | sudo apt-key add -
```

---

## Add Repository

```bash
echo deb https://aquasecurity.github.io/trivy-repo/deb $(lsb_release -sc) main | sudo tee /etc/apt/sources.list.d/trivy.list
```

---

## Update Packages

```bash
sudo apt-get update
```

---

## Install Trivy

```bash
sudo apt-get install trivy -y
```

---

## Verify Trivy

```bash
trivy --version
```

---

# Jenkins Pipeline

The Jenkins pipeline is already available inside the project directory as a `Jenkinsfile`.

---

# Important Troubleshooting Notes

## Issue 1: sonar-scanner not found

### Error

```text
sonar-scanner: not found
```

### Fix

Use absolute path:

```bash
/opt/sonar-scanner/bin/sonar-scanner
```

---

## Issue 2: Docker Permission Denied

### Error

```text
permission denied while trying to connect to Docker daemon socket
```

### Fix

```bash
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
```

---

## Issue 3: Maven Java Version Mismatch

### Problem

Jenkins was running on Java 21 while Maven build required Java 17.

### Fix

Explicitly export Java 17 inside Jenkins pipeline.

```groovy
environment {
    JAVA17_HOME = "/usr/lib/jvm/java-17-openjdk-amd64"
}
```

And use:

```bash
export JAVA_HOME=$JAVA17_HOME
export PATH=$JAVA_HOME/bin:$PATH
```

---

## Issue 4: Dockerfile JAR Name Mismatch

### Problem

Generated JAR names were different from Dockerfile COPY statements.

### Fix

Use wildcard:

```dockerfile
COPY target/*.jar app.jar
```

---

# Verify Pipeline Success

## SonarQube Dashboard

```text
http://13.206.83.5:9000
```

## Jenkins Dashboard

```text
http://13.206.83.5:8080
```

---

# Future Enhancements

- Docker Hub Push
- Kubernetes Deployment
- Helm Charts
- ArgoCD
- Prometheus Monitoring
- Grafana Dashboard
- Blue-Green Deployment
- GitOps Pipeline

---

# Author

**Dr. Sandeep Kumar Sharma**

DevOps | AWS | Azure | Kubernetes | Jenkins | Databricks Trainer
