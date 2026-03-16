package com.codecargo.jenkins

class DockerUtils implements Serializable {
    def steps

    DockerUtils(steps) {
        this.steps = steps
    }

    def loginEcr(String registry) {
        steps.sh "aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin ${registry}"
    }

    def buildAndPush(String imageName, String tag, String registry = Constants.ECR_REGISTRY) {
        def fullImage = "${registry}/${imageName}:${tag}"
        steps.sh "docker build -t ${fullImage} ."
        loginEcr(registry)
        steps.sh "docker push ${fullImage}"
        return fullImage
    }

    def scanImage(String image) {
        steps.sh "trivy image --exit-code 0 --severity HIGH,CRITICAL ${image}"
    }
}
