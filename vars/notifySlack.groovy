def call(Map config = [:]) {
    def channel = config.channel ?: '#jenkins-builds'
    def status = config.status ?: currentBuild.currentResult
    def message = config.message ?: "${env.JOB_NAME} #${env.BUILD_NUMBER} - ${status}"

    def color = status == 'SUCCESS' ? 'good' : (status == 'UNSTABLE' ? 'warning' : 'danger')

    try {
        slackSend(
            channel: channel,
            color: color,
            message: message,
            tokenCredentialId: 'slack-webhook'
        )
    } catch (Exception e) {
        echo "Slack notification failed: ${e.message}"
    }
}
