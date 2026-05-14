pipeline {

    agent any

    tools {
        maven 'maven'
    }

    environment {
        SCANNER_HOME = tool 'sonar-scanner'
    }

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

        stage('SonarQube Analysis Login Service') {
            steps {
                dir('loginservice') {
                    withSonarQubeEnv('sonarqube') {
                        sh '''
                        $SCANNER_HOME/bin/sonar-scanner \
                        -Dsonar.projectName=loginservice \
                        -Dsonar.projectKey=loginservice \
                        -Dsonar.sources=src \
                        -Dsonar.java.binaries=target/classes
                        '''
                    }
                }
            }
        }

        stage('SonarQube Analysis Catalog Service') {
            steps {
                dir('catalogservice') {
                    withSonarQubeEnv('sonarqube') {
                        sh '''
                        $SCANNER_HOME/bin/sonar-scanner \
                        -Dsonar.projectName=catalogservice \
                        -Dsonar.projectKey=catalogservice \
                        -Dsonar.sources=src \
                        -Dsonar.java.binaries=target/classes
                        '''
                    }
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
