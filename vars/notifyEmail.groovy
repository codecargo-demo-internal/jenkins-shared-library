def call(Map config = [:]) {
    def recipients = config.recipients ?: 'team@redknot-enterprises.com'
    def subject = config.subject ?: "${env.JOB_NAME} - Build #${env.BUILD_NUMBER} - ${currentBuild.currentResult}"

    emailext(
        to: recipients,
        subject: subject,
        body: '${JELLY_SCRIPT, template="html"}',
        mimeType: 'text/html',
        attachLog: config.attachLog ?: false
    )
}
