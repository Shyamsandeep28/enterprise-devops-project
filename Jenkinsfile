pipeline {

    agent any

    environment {
        JAVA17_HOME = "/usr/lib/jvm/java-17-openjdk-amd64"
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
                    sh '''
                    export JAVA_HOME=$JAVA17_HOME
                    export PATH=$JAVA_HOME/bin:$PATH

                    java -version
                    javac -version

                    mvn clean package
                    '''
                }
            }
        }

        stage('Build Catalog Service') {
            steps {
                dir('catalogservice') {
                    sh '''
                    export JAVA_HOME=$JAVA17_HOME
                    export PATH=$JAVA_HOME/bin:$PATH

                    java -version
                    javac -version

                    mvn clean package
                    '''
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
                    export JAVA_HOME=$JAVA17_HOME
                    export PATH=$JAVA_HOME/bin:$PATH

                    /opt/sonar-scanner/bin/sonar-scanner \
                    -Dsonar.projectName=loginservice \
                    -Dsonar.projectKey=loginservice \
                    -Dsonar.sources=src \
                    -Dsonar.java.binaries=target/classes \
                    -Dsonar.host.url=http://13.206.83.5:9000 \
                    -Dsonar.login=sqa_f9c575e6dabb719f19af8b36b7a89dc1e141a3b4
                    '''
                }
            }
        }

        stage('SonarQube Scan Catalog Service') {
            steps {
                dir('catalogservice') {
                    sh '''
                    export JAVA_HOME=$JAVA17_HOME
                    export PATH=$JAVA_HOME/bin:$PATH

                    /opt/sonar-scanner/bin/sonar-scanner \
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
                    sh 'docker build -t loginservice:${BUILD_NUMBER} .'
                }
            }
        }

        stage('Build Docker Image Catalog') {
            steps {
                dir('catalogservice') {
                    sh 'docker build -t catalogservice:${BUILD_NUMBER} .'
                }
            }
        }

        stage('Trivy Image Scan') {
            steps {
                sh 'trivy image loginservice:${BUILD_NUMBER}'
                sh 'trivy image catalogservice:${BUILD_NUMBER}'
            }
        }

            stage('Push Login Image') {
    steps {

        script {

            withDockerRegistry([ credentialsId: "dockerhub-creds", url: "" ]) {

                sh 'docker tag loginservice:${BUILD_NUMBER}  sandeep289/loginservice:${BUILD_NUMBER} '

                sh 'docker push sandeep289/loginservice:${BUILD_NUMBER} '
            }
        }
    }
}

stage('Push Catalog Image') {
    steps {

        script {

            withDockerRegistry([ credentialsId: "dockerhub-creds", url: "" ]) {

                sh 'docker tag catalogservice:${BUILD_NUMBER}  sandeep289/catalogservice:${BUILD_NUMBER} '

                sh 'docker push sandeep289/catalogservice:${BUILD_NUMBER}'
            }
        }
    }
}
        stage('Deploy To Kubernetes') {

    steps {

             sh '''
            kubectl apply -f kubernetes/
            '''

            sh '''
            kubectl set image deployment/login-deployment \
            login-container=sandeep289/loginservice:${BUILD_NUMBER}
            '''

            sh '''
            kubectl set image deployment/catalog-deployment \
            catalog-container=sandeep289/catalogservice:${BUILD_NUMBER}

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
