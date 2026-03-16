def call(Map config = [:]) {
    def registry = config.registry ?: com.codecargo.jenkins.Constants.ECR_REGISTRY
    def imageName = config.imageName ?: error("imageName is required")
    def tag = config.tag ?: env.BUILD_NUMBER
    def dockerfile = config.dockerfile ?: 'Dockerfile'
    def context = config.context ?: '.'

    def fullImage = "${registry}/${imageName}:${tag}"
    def latestImage = "${registry}/${imageName}:latest"

    stage('Docker Build') {
        sh "docker build -t ${fullImage} -t ${latestImage} -f ${dockerfile} ${context}"
    }

    stage('Docker Push') {
        withCredentials([usernamePassword(credentialsId: 'ecr-credentials', usernameVariable: 'AWS_USER', passwordVariable: 'AWS_PASS')]) {
            sh "echo \${AWS_PASS} | docker login --username \${AWS_USER} --password-stdin ${registry}"
            sh "docker push ${fullImage}"
            sh "docker push ${latestImage}"
        }
    }

    return fullImage
}
