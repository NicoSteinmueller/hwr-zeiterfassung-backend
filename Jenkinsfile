pipeline {
    agent any
    stages {
        stage('build & SonarQube analysis') {
            steps {
                 withSonarQubeEnv(installationName: 'sonarqube') {
                   sh "mvn clean verify sonar:sonar -Dsonar.projectKey=HWR-Zeiterfassung"
                 }
            }
        }
        stage("Quality Gate") {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    //TODO in SonarQube anschauen ob QualityGate angepasst werden muss
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}
