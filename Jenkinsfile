pipeline {

    agent any

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                url: 'https://github.com/Shyamsandeep28/enterprise-devops-project.git'
            }
        }

        stage('Build Login Service') {
            steps {
                dir('loginservice') {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Build Catalog Service') {
            steps {
                dir('catalogservice') {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Trivy File System Scan') {
            steps {
                sh 'trivy fs .'
            }
        }

        stage('SonarQube Scan Login Service') {
            steps {
                dir('loginservice') {
                    sh '''
                    sonar-scanner \
                    -Dsonar.projectName=loginservice \
                    -Dsonar.projectKey=loginservice \
                    -Dsonar.sources=src \
                    -Dsonar.java.binaries=target/classes \
                    -Dsonar.host.url=http://13.206.83.5:9000 \
                    -Dsonar.login=YOUR_SONAR_TOKEN
                    '''
                }
            }
        }

        stage('SonarQube Scan Catalog Service') {
            steps {
                dir('catalogservice') {
                    sh '''
                    sonar-scanner \
                    -Dsonar.projectName=catalogservice \
                    -Dsonar.projectKey=catalogservice \
                    -Dsonar.sources=src \
                    -Dsonar.java.binaries=target/classes \
                    -Dsonar.host.url=http://13.206.83.5:9000 \
                    -Dsonar.login=sqa_f9c575e6dabb719f19af8b36b7a89dc1e141a3b4
                    '''
                }
            }
        }

        stage('Build Docker Image Login') {
            steps {
                dir('loginservice') {
                    sh 'docker build -t loginservice:v1 .'
                }
            }
        }

        stage('Build Docker Image Catalog') {
            steps {
                dir('catalogservice') {
                    sh 'docker build -t catalogservice:v1 .'
                }
            }
        }

        stage('Trivy Image Scan') {
            steps {
                sh 'trivy image loginservice:v1'
                sh 'trivy image catalogservice:v1'
            }
        }

    }

    post {

        success {
            echo 'Pipeline Executed Successfully'
        }

        failure {
            echo 'Pipeline Failed'
        }

    }
}
