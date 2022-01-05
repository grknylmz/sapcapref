#!/usr/bin/env groovy
@Library('piper-lib') _

try {
    // pull request voting
    if (env.BRANCH_NAME.startsWith('PR')) {
        stage('Pull-request voting') {
            node {
                deleteDir()
                checkout scm

                setupPipelineEnvironment script: this, storeGithubStatistics: false

                measureDuration(script: this, measurementName: 'voter_duration') {
                    sh "cd srv && mvn clean install"

                    publishCheckResults archive: true, tasks: true, pmd: true, cpd: true, findbugs: true, checkstyle: true
                    publishTestResults junit: [updateResults: true, archive: true], jacoco: [archive: true]
                }
            }
        }
    // master pipeline
    } else if ((env.BRANCH_NAME == 'master').or(env.BRANCH_NAME == 'demo')) {
        sendNotificationSlack( script: this, channel: 'excise-duty', credentialsId: 'SLACK_EXCISE_DUTY_GLOBAL', baseUrl: 'https://sap-industry-cloud.slack.com/services/hooks/jenkins-ci/', color: 'WARNING', message: "Build & Deployment started: Job <${env.BUILD_URL}|${env.JOB_NAME}>" )

        stage('Central Build') {
            lock(resource: "${env.JOB_NAME}/10", inversePrecedence: true) {
                milestone 10
                node {
                    deleteDir()
                    checkout scm

                    setupPipelineEnvironment script: this, storeGithubStatistics: true

                    measureDuration(script: this, measurementName: 'build_duration') {
                        setVersion(script: this, buildTool: 'mta-workaround')
                        stashFiles(script: this) {
                            executeBuild script: this, buildType: 'xMakeStage'
                        }
                        publishCheckResults archive: true, tasks: true, pmd: true, cpd: true, findbugs: true, checkstyle: true
                        publishTestResults junit: [updateResults: true, archive: true], jacoco: [archive: true]
                    }
                }
            }
        }

        stage('add. Unit Tests') {
            lock(resource: "${env.JOB_NAME}/20", inversePrecedence: true) {
                milestone 20
                parallel(
                        'OPA5': {
                            node {
                                if (globalPipelineEnvironment.getConfigProperty('runOpaTests').toBoolean()) {
                                    deleteDir()
                                    measureDuration(script: this, measurementName: 'opa_duration') {
                                        executeOnePageAcceptanceTests script: this, buildTool: 'npm'
                                        publishTestResults junit: [pattern: '**/target/karma/**/TEST-*.xml']
                                    }
                                }
                            }
                        }, 'PerformanceUnit': {
                    node {
                        if (globalPipelineEnvironment.getConfigProperty('runPerformanceUnitTests').toBoolean()) {
                            deleteDir()
                            measureDuration(script: this, measurementName: 'perfunit_duration') {
                                executePerformanceUnitTests()
                                publishTestResults contiperf: [archive: true]
                            }
                        }
                    }
                }, failFast: false
                )
            }
        }
        if(env.BRANCH_NAME == 'master'){
            stage('End 2 End Tests') {
                lock(resource: "${env.JOB_NAME}/30", inversePrecedence: true) {
                    milestone 30
                    node {
                        downloadArtifactsFromNexus script: this, artifactType: 'mta', buildTool: 'mta', fromStaging: true
                        deployToCloudFoundry script: this, deployTool: 'mtaDeployPlugin', deployType: 'standard', cfApi: globalPipelineEnvironment.getConfigProperty('cfApiEndpoint'), cfOrg: globalPipelineEnvironment.getConfigProperty('cfOrg'), cfSpace: globalPipelineEnvironment.getConfigProperty('cfTestSpace'), mtaDeployParameters: '-f'
                        executeNewmanTests script: this, failOnError: false
                        archiveArtifacts 'target/newman/**/TEST-*.html'
                        publishTestResults junit: [pattern: '**/target/newman/**/TEST-*.xml']
                    }
                }
            }
        }

        stage('Promote') {
            //input message: 'Shall we proceed to promotion & release?'
            lock(resource: "${env.JOB_NAME}/80", inversePrecedence: true) {
                milestone 80
                node {
                    deleteDir()
                    measureDuration(script: this, measurementName: 'promote_duration') {
                        executeBuild script: this, buildType: 'xMakePromote'
                    }
                }
            }
        }

        stage('Release') {
            lock(resource: "${env.JOB_NAME}/90", inversePrecedence: true) {
                milestone 90
                node {
                    def cfTargetSpace
                    if(env.BRANCH_NAME == 'demo'){
                        cfTargetSpace = globalPipelineEnvironment.getConfigProperty('cfDemoSpace');
                    }else if(env.BRANCH_NAME == 'master'){
                        cfTargetSpace = globalPipelineEnvironment.getConfigProperty('cfProdSpace');
                    }else{
                        currentBuild.result = 'SUCCESS'
                        return;
                    }

                    measureDuration(script: this, measurementName: 'release_duration') {
                        deleteDir()
                        downloadArtifactsFromNexus script: this, artifactType: 'mta', buildTool: 'mta'
                        deployToCloudFoundry script: this, deployTool: 'mtaDeployPlugin', deployType: 'standard', cfApi: globalPipelineEnvironment.getConfigProperty('cfApiEndpoint'), cfOrg: globalPipelineEnvironment.getConfigProperty('cfOrg'), cfSpace: cfTargetSpace, mtaDeployParameters: '-f'
                        executeHealthCheck url: globalPipelineEnvironment.getConfigProperty('healthCheckUrlProduction')
                        currentBuild.result = 'SUCCESS'
                    }
                }
            }
        }
    }
} catch (Throwable err) { // catch all exceptions
    globalPipelineEnvironment.addError(this, err)
    throw err
} finally {
    node{
        writeInflux script: this
        sendNotificationSlack( script: this, channel: 'excise-duty', credentialsId: 'SLACK_EXCISE_DUTY_GLOBAL', baseUrl: 'https://sap-industry-cloud.slack.com/services/hooks/jenkins-ci/' )
    }
}