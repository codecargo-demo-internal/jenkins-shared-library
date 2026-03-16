def call(Map config = [:]) {
    def goVersion = config.goVersion ?: 'go-1.22'
    pipeline {
        agent any
        tools {
            go goVersion
        }
        options {
            timestamps()
            timeout(time: 20, unit: 'MINUTES')
        }
        environment {
            GOPATH = "${WORKSPACE}/go"
            CGO_ENABLED = '0'
        }
        stages {
            stage('Vet') {
                steps {
                    sh 'go vet ./...'
                }
            }
            stage('Lint') {
                steps {
                    sh 'golangci-lint run ./...'
                }
            }
            stage('Test') {
                steps {
                    sh 'go test -v -coverprofile=coverage.out ./...'
                    sh 'go tool cover -html=coverage.out -o coverage.html'
                }
                post {
                    always {
                        publishHTML(target: [
                            reportDir: '.',
                            reportFiles: 'coverage.html',
                            reportName: 'Go Coverage Report'
                        ])
                    }
                }
            }
            stage('Build') {
                steps {
                    sh 'go build -o bin/app ./cmd/...'
                }
            }
        }
    }
}
