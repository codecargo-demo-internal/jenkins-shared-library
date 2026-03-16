def call(Map config = [:]) {
    def fromEnv = config.fromEnv ?: error("fromEnv is required")
    def toEnv = config.toEnv ?: error("toEnv is required")
    def services = config.services ?: error("services list is required")
    def version = config.version ?: error("version is required")

    def fromRegistry = com.codecargo.jenkins.Constants.getRegistry(fromEnv)
    def toRegistry = com.codecargo.jenkins.Constants.getRegistry(toEnv)

    stage("Promote ${fromEnv} -> ${toEnv}") {
        services.each { svc ->
            sh """
                docker pull ${fromRegistry}/${svc}:${version}
                docker tag ${fromRegistry}/${svc}:${version} ${toRegistry}/${svc}:${version}
                docker push ${toRegistry}/${svc}:${version}
            """
        }
    }
}
