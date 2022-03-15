pipeline {
    agent any
    stages {
        stage('Scan') {
            steps {
                 withSonarQubeEnv(installationName: 'sonarqube') {
                   sh "/mvnw clean verify sonar:sonar -Dsonar.projectKey=HWR-Zeiterfassung"
                 }
            }
        }
    }
}
