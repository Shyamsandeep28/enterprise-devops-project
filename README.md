<img width="1536" height="1024" alt="ChatGPT Image May 14, 2026, 04_39_16 PM" src="https://github.com/user-attachments/assets/bf892b1c-e54d-44db-bea8-77b342f1219f" />




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
- Kubernetes

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
Docker Push to registery
      ↓

Kubernetes Deployment

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
| Kubernetes | Deployement |
 

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

---

# Kubernetes Deployment Integration

## Overview

The next phase of this project is Kubernetes deployment integration.

A separate **3-node Kubernetes Cluster** has been configured, and Jenkins will deploy the Docker images directly to the Kubernetes cluster.

Since Jenkins is running on a separate EC2 machine, Jenkins requires Kubernetes cluster access using the Kubernetes config file (`kubeconfig`).

The following setup enables Jenkins to:

- Access Kubernetes Cluster
- Execute `kubectl` commands
- Deploy applications automatically
- Manage Kubernetes Deployments and Services

---

# Kubernetes Cluster Details

| Component | Description |
|---|---|
| Master Node | Kubernetes Control Plane |
| Worker Node 1 | Application Workloads |
| Worker Node 2 | Application Workloads |
| Jenkins Server | Separate EC2 Instance |

---

# Install kubectl on Jenkins Server

## Download kubectl

```bash
curl -LO "https://dl.k8s.io/release/$(curl -L -s \
https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
```

---

## Make kubectl Executable

```bash
chmod +x kubectl
```

---

## Move kubectl Binary

```bash
sudo mv kubectl /usr/local/bin/
```

---

## Verify kubectl Installation

```bash
kubectl version --client
```

---

# Copy Kubernetes Config File to Jenkins Server

The Kubernetes cluster access configuration file is available on the Kubernetes master node.

Location:

```text
~/.kube/config
```

Copy this file from Kubernetes Master Node to Jenkins Server.

---

## Create .kube Directory

```bash
mkdir -p ~/.kube
```

---

## Copy kubeconfig File

Run from Jenkins Server:

```bash
scp ubuntu@<K8S-MASTER-IP>:~/.kube/config ~/.kube/config
```

Example:

```bash
scp ubuntu@192.168.1.10:~/.kube/config ~/.kube/config
```

---

# Verify Kubernetes Cluster Access

Run:

```bash
kubectl get nodes
```

Expected Output:

```text
NAME           STATUS   ROLES           AGE   VERSION
master-node    Ready    control-plane   XXd   v1.xx.x
worker-node1   Ready    <none>          XXd   v1.xx.x
worker-node2   Ready    <none>          XXd   v1.xx.x
```

---

# Provide Kubernetes Access to Jenkins User

By default, Jenkins user cannot access the Kubernetes config file.

We need to copy the kubeconfig file to Jenkins user home directory and provide proper permissions.

---

## Switch to Root User

```bash
sudo su -
```

---

## Create Jenkins .kube Directory

```bash
mkdir -p /var/lib/jenkins/.kube
```

---

## Copy kubeconfig to Jenkins Directory

```bash
cp ~/.kube/config /var/lib/jenkins/.kube/config
```

---

## Change Ownership

```bash
chown -R jenkins:jenkins /var/lib/jenkins/.kube
```

---

## Set Proper Permissions

```bash
chmod 600 /var/lib/jenkins/.kube/config
```

---

# Verify Kubernetes Access for Jenkins User

Switch to Jenkins user:

```bash
sudo su - jenkins
```

Run:

```bash
kubectl get nodes
```

If nodes are visible successfully, Jenkins can now deploy applications to Kubernetes cluster.

---

# Kubernetes Deployment YAML Files

The project contains Kubernetes manifests for deployment and services.

Example structure:

```text
k8s/

├── login-deployment.yaml
├── login-service.yaml
├── catalog-deployment.yaml
└── catalog-service.yaml
```

---

# Example Login Deployment

```yaml
apiVersion: apps/v1
kind: Deployment

metadata:
  name: login-deployment

spec:
  replicas: 2

  selector:
    matchLabels:
      app: loginapp

  template:
    metadata:
      labels:
        app: loginapp

    spec:
      containers:
      - name: login-container
        image: loginservice:v1

        ports:
        - containerPort: 8081
```

---

# Example Login Service

```yaml
apiVersion: v1
kind: Service

metadata:
  name: login-service

spec:
  type: NodePort

  selector:
    app: loginapp

  ports:
  - port: 8081
    targetPort: 8081
    nodePort: 30081
```

---

# Deploy Applications to Kubernetes

## Deploy Login Service

```bash
kubectl apply -f login-deployment.yaml
kubectl apply -f login-service.yaml
```

---

## Deploy Catalog Service

```bash
kubectl apply -f catalog-deployment.yaml
kubectl apply -f catalog-service.yaml
```

---

# Verify Kubernetes Deployments

## Check Deployments

```bash
kubectl get deployments
```

---

## Check Pods

```bash
kubectl get pods
```

---

## Check Services

```bash
kubectl get svc
```

---

# Access Applications via Kubernetes

## Login Service

```text
http://<WORKER-NODE-IP>:30081
```

## Catalog Service

```text
http://<WORKER-NODE-IP>:30082
```

---

# Jenkins Kubernetes Deployment Stage

The Jenkins pipeline can now deploy applications directly into Kubernetes cluster.

Example deployment stage:

```groovy
stage('Deploy to Kubernetes') {

    steps {

        sh 'kubectl apply -f k8s/login-deployment.yaml'
        sh 'kubectl apply -f k8s/login-service.yaml'

        sh 'kubectl apply -f k8s/catalog-deployment.yaml'
        sh 'kubectl apply -f k8s/catalog-service.yaml'
    }
}
```

---

# Verify End-to-End CI/CD Flow

```text
Developer
    ↓
GitHub Push
    ↓
Jenkins Pipeline Trigger
    ↓
Maven Build
    ↓
Trivy Scan
    ↓
SonarQube Analysis
    ↓
Docker Build
    ↓
Kubernetes Deployment
    ↓
Application Live on Kubernetes Cluster
```

---

# Future Kubernetes Enhancements

- Kubernetes Ingress Controller
- NGINX Ingress
- Helm Charts
- Horizontal Pod Autoscaler (HPA)
- ArgoCD GitOps Deployment
- Prometheus Monitoring
- Grafana Dashboard
- Kubernetes RBAC
- Persistent Volumes
- Secret Management
- Kubernetes Network Policies

---

---

# Author

**Dr. Sandeep Kumar Sharma**

DevOps | AWS | Azure | Kubernetes | Jenkins | Databricks Trainer
