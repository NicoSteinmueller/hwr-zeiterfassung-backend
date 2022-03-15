void setBuildStatus(String message, String context, String state) {
  // add a Github access token as a global 'secret text' credential on Jenkins with the id 'github-commit-status-token'
    withCredentials([string(credentialsId: 'GithubAccessToken', variable: 'TOKEN')]) {
      // 'set -x' for debugging. Don't worry the access token won't be actually logged
      // Also, the sh command actually executed is not properly logged, it will be further escaped when written to the log
        sh """
            set -x
            curl \"https://api.github.com/repos/org/repo/statuses/$GIT_COMMIT?access_token=$TOKEN\" \
                -H \"Content-Type: application/json\" \
                -X POST \
                -d \"{\\\"description\\\": \\\"$message\\\", \\\"state\\\": \\\"$state\\\", \\\"context\\\": \\\"$context\\\", \\\"target_url\\\": \\\"$BUILD_URL\\\"}\"
        """
    }
}

pipeline {
    agent any
    stages {
        stage('Stage') {
            steps {
                setBuildStatus("Compiling", "compile", "pending");
                script {
                    try {
                        // do the build here
                        setBuildStatus("Build complete", "compile", "success");
                    } catch (err) {
                        setBuildStatus("Failed", "pl-compile", "failure");
                        throw err
                    }
                }
            }
        }
    }
}