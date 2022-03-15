pipeline {
    stages {
        stage('Scan') {
            steps {
                def mvn = tool 'Maven';
                    withSonarQubeEnv() {
                      sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=HWR-Zeiterfassung"
                    }
            }
        }
    }
}
