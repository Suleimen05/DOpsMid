pipeline {
    agent any

    options {
        skipDefaultCheckout()
        timestamps()
    }

    triggers {
        pollSCM('H/5 * * * *')
    }

    environment {
        IMAGE_NAME = 'suleimen-yeldossuly-student-api:v1.0.1-38320'
        STUDENT_NAME = 'Suleimen Yeldossuly'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "Student: ${env.STUDENT_NAME}"
                sh 'git rev-parse --short HEAD'
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x gradlew && ./gradlew clean classes --no-daemon'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test --no-daemon'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t "$IMAGE_NAME" .'
            }
        }

        stage('Verify') {
            steps {
                sh 'docker image inspect "$IMAGE_NAME" --format "Image ID: {{.Id}}"'
                echo "Verified image for ${env.STUDENT_NAME}: ${env.IMAGE_NAME}"
            }
        }
    }
}
