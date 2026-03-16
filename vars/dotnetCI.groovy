def call(Map config = [:]) {
    def framework = config.framework ?: 'net10.0'
    def solution = config.solution ?: '.'
    pipeline {
        agent any
        options {
            timestamps()
            timeout(time: 25, unit: 'MINUTES')
        }
        stages {
            stage('Restore') {
                steps {
                    sh "dotnet restore ${solution}"
                }
            }
            stage('Build') {
                steps {
                    sh "dotnet build ${solution} --no-restore -c Release"
                }
            }
            stage('Test') {
                steps {
                    sh "dotnet test ${solution} --no-build -c Release --logger trx --results-directory TestResults"
                }
                post {
                    always {
                        junit 'TestResults/**/*.trx'
                    }
                }
            }
            stage('Format Check') {
                steps {
                    sh "dotnet format ${solution} --verify-no-changes"
                }
            }
            stage('Publish') {
                when { branch 'main' }
                steps {
                    sh "dotnet publish ${solution} -c Release -o publish/"
                }
            }
        }
    }
}
