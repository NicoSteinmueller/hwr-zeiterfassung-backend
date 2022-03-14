pipeline {
    agent any

    stages {
        stage('Hello') {
            steps {
                echo 'Hello World';
                setBuildStatus("Build succeeded", "SUCCESS");
            }
        }
    }

     post {
        success {
            setBuildStatus("Build succeeded", "SUCCESS");
        }
        failure {
            setBuildStatus("Build failed", "FAILURE");
        }
      }
}
