def call(Map config = [:]) {
    pipeline {
        agent any
        options {
            timestamps()
            timeout(time: 30, unit: 'MINUTES')
            buildDiscarder(logRotator(numToKeepStr: '20'))
        }
        stages {
            stage('Checkout') {
                steps {
                    checkout scm
                }
            }
            stage('Build') {
                steps {
                    script {
                        if (config.buildCommand) {
                            sh config.buildCommand
                        } else {
                            error "buildCommand is required"
                        }
                    }
                }
            }
            stage('Test') {
                steps {
                    script {
                        if (config.testCommand) {
                            sh config.testCommand
                        } else {
                            echo "No test command specified, skipping"
                        }
                    }
                }
            }
            stage('Publish') {
                when { branch 'main' }
                steps {
                    script {
                        if (config.publishCommand) {
                            sh config.publishCommand
                        }
                    }
                }
            }
        }
        post {
            failure {
                notifySlack(channel: config.slackChannel ?: '#builds', status: 'FAILURE')
            }
            success {
                notifySlack(channel: config.slackChannel ?: '#builds', status: 'SUCCESS')
            }
        }
    }
}
