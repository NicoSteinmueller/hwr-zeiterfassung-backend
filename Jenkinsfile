pipeline {
    agent any
    stages {
        stage('Scan') {
            node {
               def mvn = tool 'Maven';
               withSonarQubeEnv() {
                 sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=HWR-Zeiterfassung"
               }
            }
        }
    }
}
