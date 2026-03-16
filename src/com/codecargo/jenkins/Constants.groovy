package com.codecargo.jenkins

class Constants {
    static final String ECR_REGISTRY = '145023098958.dkr.ecr.us-east-2.amazonaws.com'
    static final String ECR_REGISTRY_STAGING = '234189402038.dkr.ecr.us-east-2.amazonaws.com'
    static final String ECR_REGISTRY_PROD = '794272871044.dkr.ecr.us-east-2.amazonaws.com'

    static final String SLACK_CHANNEL_BUILDS = '#jenkins-builds'
    static final String SLACK_CHANNEL_DEPLOYS = '#deployments'
    static final String SLACK_CHANNEL_ALERTS = '#platform-alerts'

    static final String TEAM_EMAIL_PLATFORM = 'platform-team@redknot-enterprises.com'
    static final String TEAM_EMAIL_DATA = 'data-engineering@redknot-enterprises.com'
    static final String TEAM_EMAIL_LEGACY = 'legacy-support@redknot-enterprises.com'

    static final String SONARQUBE_URL = 'https://sonar.dev.codecargo.dev'
    static final String ARTIFACTORY_URL = 'https://redknot.jfrog.io'
    static final String JIRA_URL = 'https://redknot.atlassian.net'

    static String getRegistry(String env) {
        switch (env) {
            case 'dev': return ECR_REGISTRY
            case 'staging': return ECR_REGISTRY_STAGING
            case 'prod': return ECR_REGISTRY_PROD
            default: return ECR_REGISTRY
        }
    }
}
