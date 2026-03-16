def call(Map config = [:]) {
    def service = config.service ?: error("service is required")
    def namespace = config.namespace ?: 'default'
    def image = config.image ?: error("image is required")
    def environment = config.environment ?: 'dev'
    def chartVersion = config.chartVersion ?: 'latest'

    stage("Deploy ${service} to ${environment}") {
        withAwsCredentials(environment: environment) {
            sh """
                kubectl set image deployment/${service} \\
                    ${service}=${image} \\
                    -n ${namespace} \\
                    --record
                kubectl rollout status deployment/${service} \\
                    -n ${namespace} \\
                    --timeout=300s
            """
        }
    }
}
