pipeline {
    agent any

    environment {
        GITHUB_ACTOR = 'jakubbbdev'
        GITHUB_TOKEN = credentials('github-credentials')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x gradlew && ./gradlew build'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }

        stage('Publish') {
            steps {
                sh './gradlew publish'
            }
        }
    }

    post {
        success {
            echo '✅ Build erfolgreich!'
        }
        failure {
            echo '❌ Build fehlgeschlagen!'
        }
    }
}
