pipeline {
    agent {
        label 'docker'
    }
    stages {
        stage('Source') {
            steps {
                git 'https://github.com/srayuso/unir-cicd.git'
            }
        }
        stage('Build') {
            steps {
                echo 'Building stage!'
                sh 'make build'
            }
        }
        stage('Unit tests') {
            steps {
                sh 'make test-unit'
                archiveArtifacts artifacts: 'results/*.xml'
            }
        }
        stage('Api tests') {
            steps {
                sh 'make test-api'
                archiveArtifacts artifacts: 'results/*.xml'
            }
        }
        stage(E2e tests') {
            steps {
                sh 'make test-e2e'
                archiveArtifacts artifacts: 'results/*.xml'
            }
        }
    }
    post {
        always {
            junit 'results/*_result.xml'
            cleanWs()
        }
        success {
            echo 'Pipeline finish successful'
        }
        failure {
            /* mail to: 'wssalgado@test.com', 
            subject: "Error in My First JenkinFile", 
            body: "Job '${JOB_NAME}' with Build Number (${BUILD_NUMBER}) is failed, at least one step failed. Please go to ${BUILD_URL} and verify the build." */
            echo 'Job (${JOB_NAME}) with Build Number (${BUILD_NUMBER}) is failed, at least one step failed. Please go to ${BUILD_URL} and verify the build.'
        }
    }
}
