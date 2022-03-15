void setBuildStatus(String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/my-org/my-repo"],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}

pipeline {
    agent any
    stages {
        stage('Stage') {
            steps {
                setBuildStatus("Build complete", "SUCCESS");
                script {
                    try {
                        // do the build here
                        setBuildStatus("Build complete", "SUCCESS");
                    } catch (err) {
                        setBuildStatus("Build complete", "SUCCESS");
                        throw err
                    }
                }
            }
        }
    }
}