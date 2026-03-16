package com.codecargo.jenkins

class KubeUtils implements Serializable {
    def steps

    KubeUtils(steps) {
        this.steps = steps
    }

    def deploy(String deployment, String image, String namespace = 'default') {
        steps.sh "kubectl set image deployment/${deployment} ${deployment}=${image} -n ${namespace}"
        steps.sh "kubectl rollout status deployment/${deployment} -n ${namespace} --timeout=300s"
    }

    def rollback(String deployment, String namespace = 'default') {
        steps.sh "kubectl rollout undo deployment/${deployment} -n ${namespace}"
    }

    def getStatus(String deployment, String namespace = 'default') {
        return steps.sh(script: "kubectl rollout status deployment/${deployment} -n ${namespace}", returnStatus: true)
    }
}
