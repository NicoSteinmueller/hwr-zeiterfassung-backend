pipeline {
    agent any
    stages {
        stage('Stage') {
            steps {
                script {
                    try {
                        echo 'test';
                    } catch (err) {
                        throw err
                    }
                }
            }
        }
        stage('SonarQube') {
            node {
              stage('SCM') {
                checkout scm
              }
              stage('SonarQube Analysis') {
                def mvn = tool 'Default Maven';
                withSonarQubeEnv() {
                  sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=HWR-Zeiterfassung"
                }
              }

        }
    }
}

}