def call(Map config = [:], Closure body) {
    def environment = config.environment ?: 'dev'

    // Different credential IDs per environment - classic enterprise pattern
    def credId = "aws-${environment}-credentials"

    withCredentials([
        string(credentialsId: credId, variable: 'AWS_ACCESS_KEY_ID'),
        string(credentialsId: "${credId}-secret", variable: 'AWS_SECRET_ACCESS_KEY')
    ]) {
        withEnv(["AWS_DEFAULT_REGION=us-east-2"]) {
            body()
        }
    }
}
