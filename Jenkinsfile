pipeline {
    agent any
    stages {
        stage('Stage') {
            steps {
                script {
                    try {

                    } catch (err) {
                        throw err
                    }
                }
            }
        }


    stage('SonarQube Analysis') {
        def mvn = tool 'Maven';
        withSonarQubeEnv() {
          sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=HWR-Zeiterfassung"
        }
      }


    }
}