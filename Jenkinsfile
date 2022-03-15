pipeline {
    agent any
    stages {
        stage('Scan') {
            steps {
                 withSonarQubeEnv() {
                   sh "/mvn clean verify sonar:sonar -Dsonar.projectKey=HWR-Zeiterfassung"
                 }
            }
        }
    }
}
