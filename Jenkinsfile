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
                setBuildStatus("Build complete", "failure");
                script {
                    try {
                        sleep(500000)
                        /asd
                        setBuildStatus("Build complete", "failure");
                    } catch (err) {
                        setBuildStatus("Build complete", "failure");
                        throw err
                    }
                }
            }
        }
    }
}