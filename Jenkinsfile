pipeline {
    agent any
    stages {
        stage('Scan') {
            steps {
                 withSonarQubeEnv(installationName: 'sonarqube') {
                   sh "mvn clean verify sonar:sonar -Dsonar.projectKey=HWR-Zeiterfassung"
                 }
            }
        }
    }
}
