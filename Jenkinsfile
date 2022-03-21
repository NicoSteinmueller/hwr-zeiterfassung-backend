pipeline {

environment {
registry = "stier09/hwr-zeiterfassung-backend"
registryCredential = 'dockerhub-credentials'
dockerImage = ''
}



    agent any

tools {
    jdk 'JDK17'
    maven 'Maven'
}

    stages {

        stage('clean Project') {
            steps {
                sh "java --version"
                sh "mvn --version"
                sh "mvn clean"
            }
        }

        stage('validate Project') {
            steps {
                sh "mvn validate"
            }
        }

        stage('compile Project') {
            steps {
                sh "mvn compile"
            }
        }

        stage('test Project') {
            steps {
                sh "mvn test"
            }
        }

        stage('package Project') {
            steps {
                sh "mvn package"
            }
        }

/*        stage('SonarQube analysis') {
            steps {
                 withSonarQubeEnv(installationName: 'sonarqube') {
                   sh "mvn sonar:sonar -Dsonar.projectKey=HWR-Zeiterfassung"
                 }
            }
        }

        stage("Quality Gate") {
            steps {
                script {
                    timeout(time: 2, unit: 'MINUTES') {
                        //TODO in SonarQube anschauen ob QualityGate angepasst werden muss
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            //error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                    }
                }

            }
        }*/

        stage('Dependency Check') {
            when {
                environment name: 'BRANCH_NAME', value: 'release'
            }
            steps {
                //sh "mvndependency-check:check"
                echo env.BRANCH_NAME
            }
        }

       stage('Build Docker Image') {
        when{
            anyOf {
                environment name: 'BRANCH_NAME', value: 'develop'
                environment name: 'BRANCH_NAME', value: 'release'
            }
        }
            steps{
                script{
                    echo env.BUILD_TAG.replaceAll(" ","_")
                    dockerImage = docker.build registry +":"+ env.BUILD_TAG.replaceAll(" ","_")
                    echo "test"

                }
            }
        }

       stage('Push Docker Image') {
        when{
            anyOf {
                environment name: 'BRANCH_NAME', value: 'develop'
                environment name: 'BRANCH_NAME', value: 'release'
            }
        }
           steps{
               script{
                   docker.withRegistry('','dockerhub-credentials'){
                       dockerImage.push()
                   }
               }
           }
       }

       stage('Push Docker Image latest') {
           when {
               environment name: 'BRANCH_NAME', value: 'release'
           }
           steps{
               script{
                   docker.withRegistry('','dockerhub-credentials'){
                       dockerImage.push('latest')
                   }
               }
           }
       }


    }
}
