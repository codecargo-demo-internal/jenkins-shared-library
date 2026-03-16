def call(Map config = [:]) {
    def projectKey = config.projectKey ?: error("projectKey is required")
    def projectName = config.projectName ?: projectKey

    stage('SonarQube Analysis') {
        withSonarQubeEnv('SonarQube') {
            sh """
                sonar-scanner \\
                    -Dsonar.projectKey=${projectKey} \\
                    -Dsonar.projectName=${projectName} \\
                    -Dsonar.sources=src \\
                    -Dsonar.host.url=\${SONAR_HOST_URL} \\
                    -Dsonar.login=\${SONAR_AUTH_TOKEN}
            """
        }
    }

    stage('Quality Gate') {
        timeout(time: 5, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: false
        }
    }
}
