def call(Map config = [:]) {
    def nodeVersion = config.nodeVersion ?: 'node-18'
    pipeline {
        agent any
        tools {
            nodejs nodeVersion
        }
        options {
            timestamps()
            timeout(time: 20, unit: 'MINUTES')
        }
        stages {
            stage('Install') {
                steps {
                    sh 'npm ci'
                }
            }
            stage('Lint') {
                steps {
                    sh 'npm run lint'
                }
            }
            stage('Test') {
                steps {
                    sh 'npm test -- --coverage'
                }
                post {
                    always {
                        publishHTML(target: [
                            reportDir: 'coverage/lcov-report',
                            reportFiles: 'index.html',
                            reportName: 'Coverage Report'
                        ])
                    }
                }
            }
            stage('Build') {
                steps {
                    sh 'npm run build'
                }
            }
            stage('Audit') {
                steps {
                    catchError(buildResult: 'UNSTABLE', stageResult: 'UNSTABLE') {
                        sh 'npm audit --production'
                    }
                }
            }
        }
    }
}
