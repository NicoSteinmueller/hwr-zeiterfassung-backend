pipeline {
    agent any
    stages {
        stage('Stage') {
            steps {
                script {
                    try {
                        sleep(500000)
                    } catch (err) {
                        throw err
                    }
                }
            }
        }
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



